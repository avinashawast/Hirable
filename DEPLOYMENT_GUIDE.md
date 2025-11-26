# Hirable Deployment Guide

This guide provides detailed instructions for deploying Hirable to production environments.

## Table of Contents

- [Pre-Deployment Checklist](#pre-deployment-checklist)
- [Environment Configuration](#environment-configuration)
- [Backend Deployment](#backend-deployment)
- [Frontend Deployment](#frontend-deployment)
- [Database Migration](#database-migration)
- [Security Hardening](#security-hardening)
- [Monitoring and Maintenance](#monitoring-and-maintenance)
- [Rollback Procedures](#rollback-procedures)

## Pre-Deployment Checklist

Before deploying to production, ensure:

- [ ] All tests pass: `mvn test` (backend) and `npm test` (frontend)
- [ ] Code review completed
- [ ] Security scan completed
- [ ] Database backups configured
- [ ] Monitoring and alerting configured
- [ ] SSL/TLS certificates obtained
- [ ] Production database created and tested
- [ ] File storage directory created with proper permissions
- [ ] Environment variables configured
- [ ] Load balancer configured (if applicable)

## Environment Configuration

### Backend Production Configuration

Edit `backend/src/main/resources/application-prod.properties`:

```properties
# Database - MUST UPDATE
spring.datasource.url=jdbc:postgresql://prod-db-host:5432/hirable
spring.datasource.username=prod_hirable_user
spring.datasource.password=STRONG_PASSWORD_HERE

# JWT - MUST UPDATE
hirable.jwt.secret=GENERATE_STRONG_SECRET_KEY_HERE

# File Storage - MUST UPDATE
hirable.file.upload-dir=/var/hirable/uploads/resumes

# WebSocket - MUST UPDATE
spring.websocket.allowed-origins=https://yourdomain.com,https://www.yourdomain.com

# CORS - MUST UPDATE
spring.web.cors.allowed-origins=https://yourdomain.com,https://www.yourdomain.com
```

### Generating Strong JWT Secret

```bash
# Generate a random 256-character secret
openssl rand -base64 32
```

### Environment Variables

Set these environment variables on your production server:

```bash
export SPRING_PROFILES_ACTIVE=prod
export SPRING_DATASOURCE_URL=jdbc:postgresql://prod-db-host:5432/hirable
export SPRING_DATASOURCE_USERNAME=prod_hirable_user
export SPRING_DATASOURCE_PASSWORD=your_strong_password
export HIRABLE_JWT_SECRET=your_generated_secret
export HIRABLE_FILE_UPLOAD_DIR=/var/hirable/uploads/resumes
```

## Backend Deployment

### 1. Build the Backend

```bash
cd backend
mvn clean package -DskipTests -P prod
```

### 2. Create Application Directory

```bash
sudo mkdir -p /opt/hirable
sudo mkdir -p /var/hirable/uploads/resumes
sudo chown -R hirable:hirable /opt/hirable
sudo chown -R hirable:hirable /var/hirable
sudo chmod 755 /var/hirable/uploads/resumes
```

### 3. Copy JAR File

```bash
sudo cp target/hirable-backend-1.0.0.jar /opt/hirable/
```

### 4. Create Systemd Service File

Create `/etc/systemd/system/hirable-backend.service`:

```ini
[Unit]
Description=Hirable Backend Service
After=network.target postgresql.service

[Service]
Type=simple
User=hirable
WorkingDirectory=/opt/hirable
Environment="SPRING_PROFILES_ACTIVE=prod"
Environment="SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/hirable"
Environment="SPRING_DATASOURCE_USERNAME=prod_hirable_user"
Environment="SPRING_DATASOURCE_PASSWORD=your_password"
Environment="HIRABLE_JWT_SECRET=your_secret"
Environment="HIRABLE_FILE_UPLOAD_DIR=/var/hirable/uploads/resumes"
ExecStart=/usr/bin/java -jar hirable-backend-1.0.0.jar
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

### 5. Start the Service

```bash
sudo systemctl daemon-reload
sudo systemctl enable hirable-backend
sudo systemctl start hirable-backend
sudo systemctl status hirable-backend
```

### 6. View Logs

```bash
sudo journalctl -u hirable-backend -f
```

## Frontend Deployment

### 1. Build the Frontend

```bash
cd frontend
npm install
npm run build
```

### 2. Configure Web Server (Nginx Example)

Create `/etc/nginx/sites-available/hirable`:

```nginx
server {
    listen 80;
    server_name yourdomain.com www.yourdomain.com;
    
    # Redirect HTTP to HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name yourdomain.com www.yourdomain.com;
    
    # SSL Configuration
    ssl_certificate /etc/letsencrypt/live/yourdomain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/yourdomain.com/privkey.pem;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;
    
    # Security Headers
    add_header Strict-Transport-Security "max-age=31536000; includeSubDomains" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-XSS-Protection "1; mode=block" always;
    
    # Frontend
    root /var/www/hirable;
    index index.html;
    
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    # API Proxy
    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
    
    # WebSocket
    location /ws {
        proxy_pass http://localhost:8080/ws;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
    
    # Cache static assets
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

### 3. Deploy Frontend Files

```bash
sudo mkdir -p /var/www/hirable
sudo cp -r frontend/dist/hirable-frontend/* /var/www/hirable/
sudo chown -R www-data:www-data /var/www/hirable
sudo chmod -R 755 /var/www/hirable
```

### 4. Enable Nginx Site

```bash
sudo ln -s /etc/nginx/sites-available/hirable /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

### 5. Setup SSL Certificate (Let's Encrypt)

```bash
sudo apt-get install certbot python3-certbot-nginx
sudo certbot certonly --nginx -d yourdomain.com -d www.yourdomain.com
```

## Database Migration

### 1. Create Production Database

```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE hirable;

# Create user
CREATE USER prod_hirable_user WITH PASSWORD 'strong_password_here';

# Grant privileges
ALTER ROLE prod_hirable_user WITH CREATEDB;
GRANT ALL PRIVILEGES ON DATABASE hirable TO prod_hirable_user;

# Exit
\q
```

### 2. Run Migrations

Migrations run automatically on backend startup. To verify:

```bash
psql -U prod_hirable_user -d hirable -c "SELECT * FROM flyway_schema_history;"
```

### 3. Backup Database

```bash
# Create backup
pg_dump -U prod_hirable_user -d hirable > hirable_backup_$(date +%Y%m%d_%H%M%S).sql

# Restore from backup
psql -U prod_hirable_user -d hirable < hirable_backup_20240101_120000.sql
```

## Security Hardening

### 1. Update JWT Secret

Generate and set a strong JWT secret:

```bash
# Generate secret
openssl rand -base64 32

# Update in application-prod.properties or environment variable
export HIRABLE_JWT_SECRET=your_generated_secret
```

### 2. Database Security

```bash
# Restrict PostgreSQL access
sudo ufw allow from 127.0.0.1 to any port 5432

# Create read-only user for backups
CREATE USER backup_user WITH PASSWORD 'backup_password';
GRANT CONNECT ON DATABASE hirable TO backup_user;
GRANT USAGE ON SCHEMA public TO backup_user;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO backup_user;
```

### 3. File Upload Security

```bash
# Set proper permissions
sudo chmod 750 /var/hirable/uploads/resumes

# Disable script execution
sudo chattr +i /var/hirable/uploads/resumes
```

### 4. Firewall Configuration

```bash
# Allow only necessary ports
sudo ufw default deny incoming
sudo ufw default allow outgoing
sudo ufw allow 22/tcp    # SSH
sudo ufw allow 80/tcp    # HTTP
sudo ufw allow 443/tcp   # HTTPS
sudo ufw enable
```

### 5. HTTPS/TLS Configuration

- Use TLS 1.2 or higher
- Use strong cipher suites
- Enable HSTS (HTTP Strict Transport Security)
- Renew certificates before expiration

## Monitoring and Maintenance

### 1. Application Monitoring

```bash
# Check backend status
sudo systemctl status hirable-backend

# View logs
sudo journalctl -u hirable-backend -f

# Check resource usage
top -p $(pgrep -f hirable-backend)
```

### 2. Database Monitoring

```bash
# Check database size
psql -U prod_hirable_user -d hirable -c "SELECT pg_size_pretty(pg_database_size('hirable'));"

# Check active connections
psql -U prod_hirable_user -d hirable -c "SELECT count(*) FROM pg_stat_activity;"

# Check slow queries
psql -U prod_hirable_user -d hirable -c "SELECT * FROM pg_stat_statements ORDER BY mean_time DESC LIMIT 10;"
```

### 3. Disk Space Monitoring

```bash
# Check disk usage
df -h

# Check upload directory size
du -sh /var/hirable/uploads/resumes

# Archive old files
find /var/hirable/uploads/resumes -mtime +90 -exec tar -czf archive_{}.tar.gz {} \;
```

### 4. Regular Backups

Create a backup script `/opt/hirable/backup.sh`:

```bash
#!/bin/bash
BACKUP_DIR="/var/backups/hirable"
DATE=$(date +%Y%m%d_%H%M%S)

mkdir -p $BACKUP_DIR

# Database backup
pg_dump -U prod_hirable_user -d hirable | gzip > $BACKUP_DIR/db_backup_$DATE.sql.gz

# File storage backup
tar -czf $BACKUP_DIR/files_backup_$DATE.tar.gz /var/hirable/uploads/resumes

# Keep only last 30 days
find $BACKUP_DIR -mtime +30 -delete

echo "Backup completed: $DATE"
```

Schedule with cron:

```bash
# Run daily at 2 AM
0 2 * * * /opt/hirable/backup.sh
```

## Rollback Procedures

### 1. Rollback Backend

```bash
# Stop current version
sudo systemctl stop hirable-backend

# Restore previous JAR
sudo cp /opt/hirable/backups/hirable-backend-1.0.0-previous.jar /opt/hirable/hirable-backend-1.0.0.jar

# Start service
sudo systemctl start hirable-backend

# Verify
sudo systemctl status hirable-backend
```

### 2. Rollback Frontend

```bash
# Restore previous build
sudo rm -rf /var/www/hirable/*
sudo cp -r /var/www/hirable-backups/dist/* /var/www/hirable/

# Restart web server
sudo systemctl restart nginx
```

### 3. Rollback Database

```bash
# Stop backend
sudo systemctl stop hirable-backend

# Restore from backup
psql -U prod_hirable_user -d hirable < /var/backups/hirable/db_backup_20240101_020000.sql.gz

# Start backend
sudo systemctl start hirable-backend
```

## Troubleshooting Production Issues

### Backend Won't Start

```bash
# Check logs
sudo journalctl -u hirable-backend -n 50

# Verify database connection
psql -U prod_hirable_user -d hirable -c "SELECT 1;"

# Check file permissions
ls -la /var/hirable/uploads/resumes
```

### High Memory Usage

```bash
# Check Java process
ps aux | grep java

# Increase heap size in systemd service
Environment="JAVA_OPTS=-Xmx2g -Xms1g"
```

### Database Connection Pool Exhausted

```bash
# Check active connections
psql -U prod_hirable_user -d hirable -c "SELECT count(*) FROM pg_stat_activity;"

# Increase pool size in application-prod.properties
spring.datasource.hikari.maximum-pool-size=30
```

### WebSocket Connection Issues

```bash
# Test WebSocket endpoint
curl -i -N -H "Connection: Upgrade" -H "Upgrade: websocket" http://localhost:8080/ws

# Check Nginx proxy configuration
sudo nginx -t
```

## Support

For issues or questions, refer to:
- Application logs: `sudo journalctl -u hirable-backend -f`
- Database logs: `/var/log/postgresql/`
- Nginx logs: `/var/log/nginx/`
- README.md for general setup instructions
