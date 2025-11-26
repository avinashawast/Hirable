# Animation System Guide

This guide documents all animations and transitions available in the Hirable UI design system.

## Overview

The animation system provides smooth, consistent animations across the application with:
- **Consistent timing**: Fast (150ms), Standard (200ms), Slow (300ms)
- **Consistent easing**: ease-in-out, ease-out, ease-in
- **Accessibility**: Respects `prefers-reduced-motion` media query
- **Performance**: Optimized for 60fps rendering

## Animation Timing

All animations use CSS variables defined in `_theme.scss`:

```scss
--animation-duration-fast: 150ms;      // Micro-interactions
--animation-duration-standard: 200ms;  // Component interactions
--animation-duration-slow: 300ms;      // Page transitions

--animation-easing-ease-in-out: cubic-bezier(0.4, 0, 0.2, 1);
--animation-easing-ease-out: cubic-bezier(0, 0, 0.2, 1);
--animation-easing-ease-in: cubic-bezier(0.4, 0, 1, 1);
```

## Page Transition Animations

### Fade + Slide Up (300ms)

Used when navigating between pages. Provides smooth entrance animation.

**CSS Classes:**
- `.page-transition-enter` - Fade in + slide up animation
- `.page-transition-exit` - Fade out + slide down animation

**Usage:**
```html
<div class="page-transition-enter">
  <router-outlet></router-outlet>
</div>
```

**Keyframes:**
```scss
@keyframes fadeInSlideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
```

## Hover Animations

### Hover Lift (200ms)

Elevates cards and containers on hover with shadow increase.

**CSS Classes:**
- `.hover-lift` - Applies lift effect on hover

**Usage:**
```html
<mat-card class="hover-lift">
  <!-- Card content -->
</mat-card>
```

**Effect:**
- Shadow increases from `shadow-sm` to `shadow-md`
- Element translates up by 4px
- Smooth 200ms transition

### Hover Highlight (200ms)

Adds background color change on hover for interactive elements.

**CSS Classes:**
- `.hover-highlight` - Applies highlight effect on hover

**Usage:**
```html
<div class="hover-highlight">
  <!-- Interactive content -->
</div>
```

## Button Press Animations

### Button Press (150ms)

Provides tactile feedback when buttons are clicked.

**CSS Classes:**
- `.button-press` - Applies press animation
- `.button-transition` - Smooth state transitions with active scale

**Usage:**
```html
<button mat-raised-button class="button-transition">
  Click Me
</button>
```

**Effect:**
- Scales to 98% on active state
- Returns to 100% after release
- Smooth 150ms animation

**Automatic Application:**
All Material buttons automatically include:
- Smooth color transitions (200ms)
- Scale effect on active (0.98)
- Focus ring styling

## Modal Entrance Animations

### Modal Fade + Scale (300ms)

Smooth entrance animation for dialogs and modals.

**CSS Classes:**
- `.modal-backdrop-enter` - Backdrop fade in
- `.modal-content-enter` - Content scale in

**Automatic Application:**
Material Dialog components automatically receive:
- Backdrop fade in (300ms)
- Content scale in from 0.95 to 1.0 (300ms)

**Keyframes:**
```scss
@keyframes modalScaleIn {
  from {
    opacity: 0;
    transform: scale(0.95);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}
```

## Loading Spinner Animations

### Spinner Rotation (Continuous)

Continuous rotation animation for loading indicators.

**CSS Classes:**
- `.spinner-rotate` - Applies rotation animation

**Usage:**
```html
<div class="spinner-rotate">
  <mat-spinner></mat-spinner>
</div>
```

### Pulse Animation (2s)

Pulsing opacity animation for loading states.

**CSS Classes:**
- `.pulse` - Applies pulse animation

**Usage:**
```html
<div class="pulse">
  Loading...
</div>
```

**Automatic Application:**
The `LoadingSpinnerComponent` automatically includes fade-in animation.

## Transition Utility Classes

### Fast Transitions (150ms)

```html
<div class="transition-fast">
  <!-- All properties transition in 150ms -->
</div>
```

### Standard Transitions (200ms)

```html
<div class="transition-standard">
  <!-- All properties transition in 200ms -->
</div>
```

### Slow Transitions (300ms)

```html
<div class="transition-slow">
  <!-- All properties transition in 300ms -->
</div>
```

### Property-Specific Transitions

**Color Transitions:**
```html
<div class="transition-color">
  <!-- Color, background-color, border-color transition -->
</div>
```

**Transform Transitions:**
```html
<div class="transition-transform">
  <!-- Transform property transitions -->
</div>
```

**Shadow Transitions:**
```html
<div class="transition-shadow">
  <!-- Box-shadow transitions -->
</div>
```

## Fade Animations

### Fade In (200ms)

```html
<div class="fade-in">
  <!-- Fades in from opacity 0 to 1 -->
</div>
```

### Fade Out (200ms)

```html
<div class="fade-out">
  <!-- Fades out from opacity 1 to 0 -->
</div>
```

## Slide Animations

### Slide In Left (200ms)

```html
<div class="slide-in-left">
  <!-- Slides in from left with fade -->
</div>
```

### Slide Out Left (200ms)

```html
<div class="slide-out-left">
  <!-- Slides out to left with fade -->
</div>
```

### Slide In Right (200ms)

```html
<div class="slide-in-right">
  <!-- Slides in from right with fade -->
</div>
```

### Slide Out Right (200ms)

```html
<div class="slide-out-right">
  <!-- Slides out to right with fade -->
</div>
```

## Scale Animations

### Scale In (200ms)

```html
<div class="scale-in">
  <!-- Scales in from 0.95 to 1.0 with fade -->
</div>
```

### Scale Out (200ms)

```html
<div class="scale-out">
  <!-- Scales out from 1.0 to 0.95 with fade -->
</div>
```

## Bounce Animation

### Bounce (200ms)

```html
<div class="bounce">
  <!-- Bounces up and down -->
</div>
```

## Component-Specific Animations

### Cards

All cards automatically include:
- Smooth shadow transitions on hover (200ms)
- Elevation changes with `hover-lift` class
- Smooth state transitions

### Buttons

All Material buttons automatically include:
- Smooth color transitions (200ms)
- Scale effect on active (0.98)
- Focus ring styling
- Disabled state transitions

### Form Fields

All form fields automatically include:
- Smooth focus transitions (200ms)
- Color transitions for labels and borders
- Error/success state animations

### Dialogs

All Material dialogs automatically include:
- Backdrop fade in (300ms)
- Content scale in (300ms)
- Smooth state transitions

### Progress Bars

All Material progress bars automatically include:
- Fade in animation (200ms)
- Smooth fill transitions

## Accessibility Considerations

### Prefers Reduced Motion

All animations respect the `prefers-reduced-motion` media query:

```scss
@media (prefers-reduced-motion: reduce) {
  *,
  *::before,
  *::after {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
  }
}
```

Users who prefer reduced motion will see:
- Animations disabled or minimized
- Instant state changes
- No motion-based feedback

### Focus Indicators

All interactive elements include visible focus indicators:
- 2px solid outline in primary color
- 2px outline offset for visibility
- Works with keyboard navigation

## Performance Optimization

### 60fps Target

All animations are optimized for 60fps performance:
- Use `transform` and `opacity` for animations (GPU-accelerated)
- Avoid animating layout properties (width, height, position)
- Use `will-change` sparingly for complex animations

### Best Practices

1. **Use CSS animations** instead of JavaScript when possible
2. **Animate only necessary properties** (transform, opacity)
3. **Keep animations short** (150-300ms)
4. **Test on lower-end devices** to ensure smooth performance
5. **Use `prefers-reduced-motion`** for accessibility

## Browser Support

All animations use standard CSS features supported in:
- Chrome/Edge 90+
- Firefox 88+
- Safari 14+
- Mobile browsers (iOS Safari 14+, Chrome Android)

## Examples

### Animated Card List

```html
<div class="card-list">
  <mat-card class="hover-lift" *ngFor="let item of items">
    <mat-card-content>
      {{ item.title }}
    </mat-card-content>
  </mat-card>
</div>
```

### Animated Form Submission

```html
<form [formGroup]="form">
  <mat-form-field>
    <mat-label>Name</mat-label>
    <input matInput formControlName="name">
  </mat-form-field>
  
  <button mat-raised-button (click)="submit()" [disabled]="isSubmitting">
    <span *ngIf="!isSubmitting">Submit</span>
    <mat-spinner *ngIf="isSubmitting" diameter="20"></mat-spinner>
  </button>
</form>
```

### Animated Page Transition

```html
<div class="router-outlet-wrapper page-transition-enter">
  <router-outlet></router-outlet>
</div>
```

### Animated Loading State

```html
<app-loading-spinner 
  [isLoading]="isLoading"
  message="Loading data...">
</app-loading-spinner>
```

## Customization

To customize animation timing globally, update CSS variables in `_theme.scss`:

```scss
:root {
  --animation-duration-fast: 100ms;      // Faster animations
  --animation-duration-standard: 150ms;
  --animation-duration-slow: 250ms;
}
```

To disable animations for a specific element:

```html
<div style="animation: none; transition: none;">
  <!-- No animations -->
</div>
```

## Files

- `_animations.scss` - Core animation keyframes and utility classes
- `_dialog-animations.scss` - Material Dialog animations
- `_progress-animations.scss` - Material Progress Bar animations
- `_theme.scss` - Animation timing and easing variables
- `_buttons.scss` - Button transition styles
- `_cards.scss` - Card transition styles
- `_forms.scss` - Form field transition styles
