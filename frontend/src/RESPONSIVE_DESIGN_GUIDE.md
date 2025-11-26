# Responsive Design Implementation Guide

## Overview

This guide documents the responsive design implementation for the Hirable platform. The design system uses a mobile-first approach with three primary breakpoints: mobile (0-599px), tablet (600-959px), and desktop (960px+).

## Breakpoints

The responsive design system uses the following breakpoints:

- **Mobile**: 0px - 599px
- **Tablet**: 600px - 959px
- **Desktop**: 960px+

These breakpoints are defined in `_theme.scss` and used throughout the design system.

## Files

### Core Responsive Files

1. **`_responsive.scss`** - Main responsive design adjustments
   - Typography scaling for each breakpoint
   - Spacing adjustments
   - Component layout changes
   - Navigation adjustments
   - Accessibility features (reduced motion, high contrast)

2. **`_responsive-utilities.scss`** - Utility classes for responsive design
   - Display utilities (hide/show on specific breakpoints)
   - Flex and grid responsive utilities
   - Width, padding, and margin utilities
   - Text alignment and font size utilities
   - Touch-friendly utilities

3. **`styles.scss`** - Global styles (imports all responsive files)

## Typography Scaling

Typography scales responsively across breakpoints:

### Mobile (0-599px)
- Base font size: 14px
- Display Large: 24px / 32px line-height
- Headline Large: 18px / 26px line-height
- Body Medium: 13px / 18px line-height

### Tablet (600-959px)
- Base font size: 15px
- Display Large: 28px / 36px line-height
- Headline Large: 20px / 28px line-height
- Body Medium: 14px / 20px line-height

### Desktop (960px+)
- Base font size: 16px
- Display Large: 32px / 40px line-height
- Headline Large: 24px / 32px line-height
- Body Medium: 14px / 20px line-height

## Spacing Adjustments

Spacing uses the 8px grid system consistently across all breakpoints:

### Mobile
- Reduced padding on components (8px instead of 16px)
- Reduced margins between sections
- Compact card layouts

### Tablet
- Medium padding (16px)
- Standard margins
- Two-column layouts where appropriate

### Desktop
- Full padding (16-24px)
- Generous margins
- Multi-column layouts

## Button Sizing

Buttons are sized for touch-friendly interaction:

### Mobile
- Minimum height: 44px
- Minimum width: 44px (for icon buttons)
- Padding: 10px 16px

### Tablet
- Minimum height: 40px
- Minimum width: 40px (for icon buttons)
- Padding: 8px 16px

### Desktop
- Minimum height: 36px
- Minimum width: 40px (for icon buttons)
- Padding: 8px 16px

## Card Layouts

Cards adapt their layout based on screen size:

### Mobile
- Single column layout
- Full width cards
- Reduced padding (8px)
- Stacked actions

### Tablet
- Two-column grid
- Adjusted spacing
- Medium padding (16px)

### Desktop
- Multi-column grid (2-4 columns depending on card-grid class)
- Full spacing
- Standard padding (16px)

## Form Layouts

Forms adapt their layout for different screen sizes:

### Mobile
- Single column layout
- Full-width form fields
- Full-width buttons
- Stacked form actions
- Minimum input height: 44px (touch-friendly)

### Tablet
- Two-column layout for multi-column rows
- Adjusted spacing
- Standard input sizing

### Desktop
- Full multi-column layout
- Standard spacing
- Standard input sizing

## Navigation

The navigation component adapts for different screen sizes:

### Mobile (0-599px)
- Header height: 56px
- Hamburger menu button visible
- Sidenav mode: "over" (overlay)
- Sidenav width: 256px
- Reduced padding and spacing

### Tablet (600-959px)
- Header height: 64px
- Hamburger menu button visible
- Sidenav mode: "over" (overlay)
- Sidenav width: 280px
- Standard padding

### Desktop (960px+)
- Header height: 64px
- Hamburger menu button hidden
- Sidenav mode: "side" (permanent)
- Sidenav width: 280px
- Standard padding

## Utility Classes

### Display Utilities

```html
<!-- Hide on mobile -->
<div class="hide-mobile">Content hidden on mobile</div>

<!-- Show only on mobile -->
<div class="show-mobile">Content visible only on mobile</div>

<!-- Hide on tablet -->
<div class="hide-tablet">Content hidden on tablet</div>

<!-- Show only on tablet -->
<div class="show-tablet">Content visible only on tablet</div>

<!-- Hide on desktop -->
<div class="hide-desktop">Content hidden on desktop</div>

<!-- Show only on desktop -->
<div class="show-desktop">Content visible only on desktop</div>
```

### Responsive Padding

```html
<!-- Responsive horizontal padding -->
<div class="px-mobile">Padding on mobile</div>
<div class="px-tablet">Padding on tablet</div>
<div class="px-desktop">Padding on desktop</div>
```

### Responsive Margins

```html
<!-- Responsive horizontal margins -->
<div class="mx-mobile">Margin on mobile</div>
<div class="mx-tablet">Margin on tablet</div>
<div class="mx-desktop">Margin on desktop</div>
```

### Responsive Width

```html
<!-- Full width on mobile -->
<div class="full-width-mobile">Full width on mobile</div>

<!-- Full width on tablet -->
<div class="full-width-tablet">Full width on tablet</div>
```

### Responsive Text Alignment

```html
<!-- Center text on mobile -->
<div class="text-center-mobile">Centered on mobile</div>

<!-- Left align on tablet -->
<div class="text-left-tablet">Left aligned on tablet</div>
```

### Responsive Font Sizes

```html
<!-- Small text on mobile -->
<div class="text-sm-mobile">Small on mobile</div>

<!-- Medium text on tablet -->
<div class="text-md-tablet">Medium on tablet</div>

<!-- Large text on desktop -->
<div class="text-lg-desktop">Large on desktop</div>
```

## SCSS Mixins

The design system provides responsive mixins for custom styles:

```scss
// Mobile only
@include mobile-only {
  // Styles for mobile
}

// Tablet and up
@include tablet-and-up {
  // Styles for tablet and desktop
}

// Tablet only
@include tablet-only {
  // Styles for tablet only
}

// Desktop and up
@include desktop-and-up {
  // Styles for desktop
}
```

## Accessibility Features

### Reduced Motion Support

The design system respects the `prefers-reduced-motion` media query:

```scss
@media (prefers-reduced-motion: reduce) {
  // Animations and transitions are disabled
}
```

This ensures that users who prefer reduced motion don't experience animations that could cause motion sickness.

### High Contrast Mode

The design system supports high contrast mode:

```scss
@media (prefers-contrast: more) {
  // Borders are thicker and more visible
  // Colors have higher contrast
}
```

### Touch-Friendly Sizing

All interactive elements on mobile have a minimum size of 44x44px to ensure they're easy to tap.

## Testing Responsive Design

### Breakpoints to Test

1. **Mobile**: 375px (iPhone SE), 414px (iPhone 12), 480px (Android)
2. **Tablet**: 768px (iPad), 820px (iPad Pro)
3. **Desktop**: 1024px, 1366px, 1920px

### Testing Tools

- Chrome DevTools (F12 → Toggle device toolbar)
- Firefox Responsive Design Mode (Ctrl+Shift+M)
- Safari Responsive Design Mode (Develop → Enter Responsive Design Mode)
- Physical devices for real-world testing

### Testing Checklist

- [ ] Navigation displays correctly on all breakpoints
- [ ] Cards stack properly on mobile
- [ ] Forms are usable on mobile (44px minimum touch targets)
- [ ] Typography is readable on all screen sizes
- [ ] Buttons are touch-friendly on mobile
- [ ] Images scale appropriately
- [ ] No horizontal scrolling on mobile
- [ ] Animations work smoothly (60fps)
- [ ] Reduced motion is respected
- [ ] High contrast mode works correctly
- [ ] Keyboard navigation works on all breakpoints
- [ ] Focus indicators are visible

## Best Practices

1. **Mobile-First Approach**: Start with mobile styles, then add breakpoints for larger screens
2. **Touch-Friendly**: Ensure all interactive elements are at least 44x44px on mobile
3. **Readable Typography**: Use appropriate font sizes for each breakpoint
4. **Flexible Layouts**: Use flexbox and grid for flexible layouts
5. **Test on Real Devices**: Always test on actual devices, not just browser emulation
6. **Performance**: Minimize CSS and use efficient selectors
7. **Accessibility**: Always consider accessibility when implementing responsive design
8. **Progressive Enhancement**: Ensure basic functionality works on all devices

## Common Patterns

### Responsive Grid

```html
<div class="card-grid">
  <div class="card">Card 1</div>
  <div class="card">Card 2</div>
  <div class="card">Card 3</div>
</div>
```

Mobile: 1 column
Tablet: 2 columns
Desktop: 3+ columns

### Responsive Form

```html
<form>
  <div class="form-field-row-2">
    <mat-form-field>
      <input matInput placeholder="Field 1">
    </mat-form-field>
    <mat-form-field>
      <input matInput placeholder="Field 2">
    </mat-form-field>
  </div>
</form>
```

Mobile: 1 column
Tablet: 2 columns
Desktop: 2 columns

### Responsive Navigation

The navigation component automatically adapts:
- Mobile: Hamburger menu with overlay sidenav
- Tablet: Hamburger menu with overlay sidenav
- Desktop: Permanent sidenav

## Troubleshooting

### Content Overflowing on Mobile

- Check that containers have `max-width: 100%`
- Ensure padding doesn't exceed available space
- Use `overflow-x: auto` for horizontal scrolling if necessary

### Text Too Small on Mobile

- Check typography scaling in `_responsive.scss`
- Ensure minimum font size is 12px
- Test with actual mobile devices

### Buttons Not Touch-Friendly

- Ensure minimum height and width are 44px on mobile
- Check padding and margins
- Test with actual touch devices

### Navigation Not Closing on Mobile

- Check that `closeSidenavOnMobile()` is called on navigation item click
- Verify breakpoint is set to 960px
- Test on actual mobile devices

## Future Enhancements

1. **Dark Mode**: Implement dark mode support with `prefers-color-scheme`
2. **Container Queries**: Use CSS container queries for component-level responsiveness
3. **Fluid Typography**: Implement fluid typography using CSS clamp()
4. **Responsive Images**: Optimize images for different screen sizes using srcset
5. **Performance**: Implement lazy loading for images and components

## References

- [MDN: Responsive Design](https://developer.mozilla.org/en-US/docs/Learn/CSS/CSS_layout/Responsive_Design)
- [Google: Mobile-Friendly Test](https://search.google.com/test/mobile-friendly)
- [W3C: Media Queries](https://www.w3.org/TR/mediaqueries-5/)
- [WCAG: Responsive Design](https://www.w3.org/WAI/WCAG21/Understanding/responsive-design)
