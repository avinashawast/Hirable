import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatTabsModule } from '@angular/material/tabs';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { TaxonomyService, TaxonomyDTO } from '../../services/taxonomy.service';

@Component({
  selector: 'app-system-config',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatTabsModule,
    MatIconModule,
    MatTooltipModule,
    MatSnackBarModule,
    MatTableModule
  ],
  templateUrl: './system-config.component.html',
  styleUrls: ['./system-config.component.css']
})
export class SystemConfigComponent implements OnInit {
  categories: TaxonomyDTO[] = [];
  industries: TaxonomyDTO[] = [];
  skills: TaxonomyDTO[] = [];

  showCategoryForm = false;
  showIndustryForm = false;
  showSkillForm = false;

  selectedCategory: TaxonomyDTO | null = null;
  selectedIndustry: TaxonomyDTO | null = null;
  selectedSkill: TaxonomyDTO | null = null;

  newCategoryName = '';
  newIndustryName = '';
  newSkillName = '';

  displayedColumns: string[] = ['id', 'name', 'createdAt', 'actions'];

  constructor(
    private taxonomyService: TaxonomyService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadCategories();
    this.loadIndustries();
    this.loadSkills();
  }

  // Category operations
  loadCategories(): void {
    this.taxonomyService.getAllCategories().subscribe({
      next: (data: any) => {
        this.categories = data;
      },
      error: (error: any) => {
        this.snackBar.open('Failed to load categories', 'Close', { duration: 3000 });
        console.error('Error loading categories:', error);
      }
    });
  }

  openCategoryForm(): void {
    this.showCategoryForm = true;
    this.newCategoryName = '';
    this.selectedCategory = null;
  }

  closeCategoryForm(): void {
    this.showCategoryForm = false;
    this.newCategoryName = '';
    this.selectedCategory = null;
  }

  createCategory(): void {
    if (!this.newCategoryName.trim()) {
      this.snackBar.open('Please enter a category name', 'Close', { duration: 3000 });
      return;
    }

    const categoryDTO: TaxonomyDTO = {
      id: 0,
      name: this.newCategoryName,
      createdAt: new Date().toISOString()
    };

    this.taxonomyService.createCategory(categoryDTO).subscribe({
      next: () => {
        this.snackBar.open('Category created successfully', 'Close', { duration: 3000 });
        this.closeCategoryForm();
        this.loadCategories();
      },
      error: (error: any) => {
        this.snackBar.open(error.error?.message || 'Failed to create category', 'Close', { duration: 3000 });
        console.error('Error creating category:', error);
      }
    });
  }

  editCategory(category: TaxonomyDTO): void {
    this.selectedCategory = { ...category };
    this.newCategoryName = category.name;
    this.showCategoryForm = true;
  }

  updateCategory(): void {
    if (!this.selectedCategory || !this.newCategoryName.trim()) {
      this.snackBar.open('Please enter a category name', 'Close', { duration: 3000 });
      return;
    }

    const categoryDTO: TaxonomyDTO = {
      ...this.selectedCategory,
      name: this.newCategoryName
    };

    this.taxonomyService.updateCategory(this.selectedCategory.id, categoryDTO).subscribe({
      next: () => {
        this.snackBar.open('Category updated successfully', 'Close', { duration: 3000 });
        this.closeCategoryForm();
        this.loadCategories();
      },
      error: (error: any) => {
        this.snackBar.open('Failed to update category', 'Close', { duration: 3000 });
        console.error('Error updating category:', error);
      }
    });
  }

  deleteCategory(id: number): void {
    if (confirm('Are you sure you want to delete this category?')) {
      this.taxonomyService.deleteCategory(id).subscribe({
        next: () => {
          this.snackBar.open('Category deleted successfully', 'Close', { duration: 3000 });
          this.loadCategories();
        },
        error: (error: any) => {
          this.snackBar.open('Failed to delete category', 'Close', { duration: 3000 });
          console.error('Error deleting category:', error);
        }
      });
    }
  }

  // Industry operations
  loadIndustries(): void {
    this.taxonomyService.getAllIndustries().subscribe({
      next: (data: any) => {
        this.industries = data;
      },
      error: (error: any) => {
        this.snackBar.open('Failed to load industries', 'Close', { duration: 3000 });
        console.error('Error loading industries:', error);
      }
    });
  }

  openIndustryForm(): void {
    this.showIndustryForm = true;
    this.newIndustryName = '';
    this.selectedIndustry = null;
  }

  closeIndustryForm(): void {
    this.showIndustryForm = false;
    this.newIndustryName = '';
    this.selectedIndustry = null;
  }

  createIndustry(): void {
    if (!this.newIndustryName.trim()) {
      this.snackBar.open('Please enter an industry name', 'Close', { duration: 3000 });
      return;
    }

    const industryDTO: TaxonomyDTO = {
      id: 0,
      name: this.newIndustryName,
      createdAt: new Date().toISOString()
    };

    this.taxonomyService.createIndustry(industryDTO).subscribe({
      next: () => {
        this.snackBar.open('Industry created successfully', 'Close', { duration: 3000 });
        this.closeIndustryForm();
        this.loadIndustries();
      },
      error: (error: any) => {
        this.snackBar.open(error.error?.message || 'Failed to create industry', 'Close', { duration: 3000 });
        console.error('Error creating industry:', error);
      }
    });
  }

  editIndustry(industry: TaxonomyDTO): void {
    this.selectedIndustry = { ...industry };
    this.newIndustryName = industry.name;
    this.showIndustryForm = true;
  }

  updateIndustry(): void {
    if (!this.selectedIndustry || !this.newIndustryName.trim()) {
      this.snackBar.open('Please enter an industry name', 'Close', { duration: 3000 });
      return;
    }

    const industryDTO: TaxonomyDTO = {
      ...this.selectedIndustry,
      name: this.newIndustryName
    };

    this.taxonomyService.updateIndustry(this.selectedIndustry.id, industryDTO).subscribe({
      next: () => {
        this.snackBar.open('Industry updated successfully', 'Close', { duration: 3000 });
        this.closeIndustryForm();
        this.loadIndustries();
      },
      error: (error: any) => {
        this.snackBar.open('Failed to update industry', 'Close', { duration: 3000 });
        console.error('Error updating industry:', error);
      }
    });
  }

  deleteIndustry(id: number): void {
    if (confirm('Are you sure you want to delete this industry?')) {
      this.taxonomyService.deleteIndustry(id).subscribe({
        next: () => {
          this.snackBar.open('Industry deleted successfully', 'Close', { duration: 3000 });
          this.loadIndustries();
        },
        error: (error: any) => {
          this.snackBar.open('Failed to delete industry', 'Close', { duration: 3000 });
          console.error('Error deleting industry:', error);
        }
      });
    }
  }

  // Skill operations
  loadSkills(): void {
    this.taxonomyService.getAllSkills().subscribe({
      next: (data: any) => {
        this.skills = data;
      },
      error: (error: any) => {
        this.snackBar.open('Failed to load skills', 'Close', { duration: 3000 });
        console.error('Error loading skills:', error);
      }
    });
  }

  openSkillForm(): void {
    this.showSkillForm = true;
    this.newSkillName = '';
    this.selectedSkill = null;
  }

  closeSkillForm(): void {
    this.showSkillForm = false;
    this.newSkillName = '';
    this.selectedSkill = null;
  }

  createSkill(): void {
    if (!this.newSkillName.trim()) {
      this.snackBar.open('Please enter a skill name', 'Close', { duration: 3000 });
      return;
    }

    const skillDTO: TaxonomyDTO = {
      id: 0,
      name: this.newSkillName,
      createdAt: new Date().toISOString()
    };

    this.taxonomyService.createSkill(skillDTO).subscribe({
      next: () => {
        this.snackBar.open('Skill created successfully', 'Close', { duration: 3000 });
        this.closeSkillForm();
        this.loadSkills();
      },
      error: (error: any) => {
        this.snackBar.open(error.error?.message || 'Failed to create skill', 'Close', { duration: 3000 });
        console.error('Error creating skill:', error);
      }
    });
  }

  editSkill(skill: TaxonomyDTO): void {
    this.selectedSkill = { ...skill };
    this.newSkillName = skill.name;
    this.showSkillForm = true;
  }

  updateSkill(): void {
    if (!this.selectedSkill || !this.newSkillName.trim()) {
      this.snackBar.open('Please enter a skill name', 'Close', { duration: 3000 });
      return;
    }

    const skillDTO: TaxonomyDTO = {
      ...this.selectedSkill,
      name: this.newSkillName
    };

    this.taxonomyService.updateSkill(this.selectedSkill.id, skillDTO).subscribe({
      next: () => {
        this.snackBar.open('Skill updated successfully', 'Close', { duration: 3000 });
        this.closeSkillForm();
        this.loadSkills();
      },
      error: (error: any) => {
        this.snackBar.open('Failed to update skill', 'Close', { duration: 3000 });
        console.error('Error updating skill:', error);
      }
    });
  }

  deleteSkill(id: number): void {
    if (confirm('Are you sure you want to delete this skill?')) {
      this.taxonomyService.deleteSkill(id).subscribe({
        next: () => {
          this.snackBar.open('Skill deleted successfully', 'Close', { duration: 3000 });
          this.loadSkills();
        },
        error: (error: any) => {
          this.snackBar.open('Failed to delete skill', 'Close', { duration: 3000 });
          console.error('Error deleting skill:', error);
        }
      });
    }
  }

  isSaveDisabled(): boolean {
    return !this.newCategoryName.trim() && !this.newIndustryName.trim() && !this.newSkillName.trim();
  }
}
