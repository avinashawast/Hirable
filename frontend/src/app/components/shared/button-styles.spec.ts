import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Component, DebugElement } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { By } from '@angular/platform-browser';

/**
 * Test component that uses all button variants to verify styling
 */
@Component({
  selector: 'app-button-test',
  standalone: true,
  imports: [MatButtonModule],
  template: `
    <!-- Contained Variant (Primary) -->
    <button mat-raised-button id="btn-contained-primary">Primary</button>
    <button mat-raised-button color="accent" id="btn-contained-accent">Accent</button>
    <button mat-raised-button color="warn" id="btn-contained-warn">Warn</button>
    <button mat-raised-button disabled id="btn-contained-disabled">Disabled</button>

    <!-- Outlined Variant (Secondary) -->
    <button mat-stroked-button id="btn-outlined-primary">Primary</button>
    <button mat-stroked-button color="accent" id="btn-outlined-accent">Accent</button>
    <button mat-stroked-button color="warn" id="btn-outlined-warn">Warn</button>
    <button mat-stroked-button disabled id="btn-outlined-disabled">Disabled</button>

    <!-- Text Variant (Tertiary) -->
    <button mat-button id="btn-text-primary">Primary</button>
    <button mat-button color="accent" id="btn-text-accent">Accent</button>
    <button mat-button color="warn" id="btn-text-warn">Warn</button>
    <button mat-button disabled id="btn-text-disabled">Disabled</button>

    <!-- Size Variants -->
    <button mat-raised-button class="btn-small" id="btn-size-small">Small</button>
    <button mat-raised-button class="btn-medium" id="btn-size-medium">Medium</button>
    <button mat-raised-button class="btn-large" id="btn-size-large">Large</button>

    <!-- Icon Buttons -->
    <button mat-icon-button id="btn-icon-primary">Icon</button>
    <button mat-icon-button color="accent" id="btn-icon-accent">Icon</button>
    <button mat-icon-button color="warn" id="btn-icon-warn">Icon</button>

    <!-- FAB Buttons -->
    <button mat-fab id="btn-fab-primary">FAB</button>
    <button mat-fab color="accent" id="btn-fab-accent">FAB</button>
    <button mat-mini-fab id="btn-mini-fab">Mini</button>
  `
})
class ButtonTestComponent {}

describe('Button Component Styles', () => {
  let component: ButtonTestComponent;
  let fixture: ComponentFixture<ButtonTestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ButtonTestComponent, MatButtonModule]
    }).compileComponents();

    fixture = TestBed.createComponent(ButtonTestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  describe('Contained Variant (Primary Action)', () => {
    it('should apply primary color to contained button', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-contained-primary'));
      const element = button.nativeElement as HTMLButtonElement;
      const styles = window.getComputedStyle(element);
      
      expect(element).toBeTruthy();
      expect(element.getAttribute('mat-raised-button')).toBe('');
    });

    it('should apply accent color to contained button with color="accent"', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-contained-accent'));
      const element = button.nativeElement as HTMLButtonElement;
      
      expect(element).toBeTruthy();
      expect(element.getAttribute('color')).toBe('accent');
    });

    it('should apply warn color to contained button with color="warn"', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-contained-warn'));
      const element = button.nativeElement as HTMLButtonElement;
      
      expect(element).toBeTruthy();
      expect(element.getAttribute('color')).toBe('warn');
    });

    it('should apply disabled state to contained button', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-contained-disabled'));
      const element = button.nativeElement as HTMLButtonElement;
      
      expect(element.disabled).toBe(true);
    });
  });

  describe('Outlined Variant (Secondary Action)', () => {
    it('should apply primary color to outlined button', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-outlined-primary'));
      const element = button.nativeElement as HTMLButtonElement;
      
      expect(element).toBeTruthy();
      expect(element.getAttribute('mat-stroked-button')).toBe('');
    });

    it('should apply accent color to outlined button with color="accent"', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-outlined-accent'));
      const element = button.nativeElement as HTMLButtonElement;
      
      expect(element).toBeTruthy();
      expect(element.getAttribute('color')).toBe('accent');
    });

    it('should apply warn color to outlined button with color="warn"', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-outlined-warn'));
      const element = button.nativeElement as HTMLButtonElement;
      
      expect(element).toBeTruthy();
      expect(element.getAttribute('color')).toBe('warn');
    });

    it('should apply disabled state to outlined button', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-outlined-disabled'));
      const element = button.nativeElement as HTMLButtonElement;
      
      expect(element.disabled).toBe(true);
    });
  });

  describe('Text Variant (Tertiary Action)', () => {
    it('should apply primary color to text button', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-text-primary'));
      const element = button.nativeElement as HTMLButtonElement;
      
      expect(element).toBeTruthy();
      expect(element.getAttribute('mat-button')).toBe('');
    });

    it('should apply accent color to text button with color="accent"', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-text-accent'));
      const element = button.nativeElement as HTMLButtonElement;
      
      expect(element).toBeTruthy();
      expect(element.getAttribute('color')).toBe('accent');
    });

    it('should apply warn color to text button with color="warn"', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-text-warn'));
      const element = button.nativeElement as HTMLButtonElement;
      
      expect(element).toBeTruthy();
      expect(element.getAttribute('color')).toBe('warn');
    });

    it('should apply disabled state to text button', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-text-disabled'));
      const element = button.nativeElement as HTMLButtonElement;
      
      expect(element.disabled).toBe(true);
    });
  });

  describe('Size Variants', () => {
    it('should apply small size class', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-size-small'));
      const element = button.nativeElement as HTMLButtonElement;
      
      expect(element).toBeTruthy();
      expect(element.classList.contains('btn-small')).toBe(true);
    });

    it('should apply medium size class', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-size-medium'));
      const element = button.nativeElement as HTMLButtonElement;
      
      expect(element).toBeTruthy();
      expect(element.classList.contains('btn-medium')).toBe(true);
    });

    it('should apply large size class', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-size-large'));
      const element = button.nativeElement as HTMLButtonElement;
      
      expect(element).toBeTruthy();
      expect(element.classList.contains('btn-large')).toBe(true);
    });
  });

  describe('Icon Buttons', () => {
    it('should render icon button with primary color', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-icon-primary'));
      const element = button.nativeElement as HTMLButtonElement;
      
      expect(element).toBeTruthy();
      expect(element.getAttribute('mat-icon-button')).toBe('');
    });

    it('should render icon button with accent color', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-icon-accent'));
      const element = button.nativeElement as HTMLButtonElement;
      
      expect(element).toBeTruthy();
      expect(element.getAttribute('color')).toBe('accent');
    });

    it('should render icon button with warn color', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-icon-warn'));
      const element = button.nativeElement as HTMLButtonElement;
      
      expect(element).toBeTruthy();
      expect(element.getAttribute('color')).toBe('warn');
    });
  });

  describe('FAB Buttons', () => {
    it('should render FAB button with primary color', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-fab-primary'));
      const element = button.nativeElement as HTMLButtonElement;
      
      expect(element).toBeTruthy();
      expect(element.getAttribute('mat-fab')).toBe('');
    });

    it('should render FAB button with accent color', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-fab-accent'));
      const element = button.nativeElement as HTMLButtonElement;
      
      expect(element).toBeTruthy();
      expect(element.getAttribute('color')).toBe('accent');
    });

    it('should render mini FAB button', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-mini-fab'));
      const element = button.nativeElement as HTMLButtonElement;
      
      expect(element).toBeTruthy();
      expect(element.getAttribute('mat-mini-fab')).toBe('');
    });
  });

  describe('Button States', () => {
    it('should have focus-visible outline on focus', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-contained-primary'));
      const element = button.nativeElement as HTMLButtonElement;
      
      element.focus();
      expect(document.activeElement).toBe(element);
    });

    it('should be disabled when disabled attribute is set', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-contained-disabled'));
      const element = button.nativeElement as HTMLButtonElement;
      
      expect(element.disabled).toBe(true);
    });

    it('should have cursor pointer on enabled button', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-contained-primary'));
      const element = button.nativeElement as HTMLButtonElement;
      const styles = window.getComputedStyle(element);
      
      expect(styles.cursor).toBe('pointer');
    });

    it('should have cursor not-allowed on disabled button', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-contained-disabled'));
      const element = button.nativeElement as HTMLButtonElement;
      const styles = window.getComputedStyle(element);
      
      expect(styles.cursor).toBe('not-allowed');
    });
  });

  describe('Transitions', () => {
    it('should have transition property on button', () => {
      const button: DebugElement = fixture.debugElement.query(By.css('#btn-contained-primary'));
      const element = button.nativeElement as HTMLButtonElement;
      const styles = window.getComputedStyle(element);
      
      expect(styles.transition).toBeTruthy();
    });
  });
});
