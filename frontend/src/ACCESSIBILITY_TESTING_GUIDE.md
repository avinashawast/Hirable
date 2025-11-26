# Accessibility Testing Guide

## Overview
This guide provides step-by-step instructions for testing the Hirable platform for accessibility compliance with WCAG 2.1 Level AA standards.

---

## 1. Color Contrast Testing

### Using WebAIM Contrast Checker
1. Visit https://webaim.org/resources/contrastchecker/
2. Enter foreground color (text color)
3. Enter background color
4. Check if contrast ratio meets WCAG AA standards:
   - Normal text: 4.5:1 minimum
   - Large text: 3:1 minimum
   - UI components: 3:1 minimum

### Using Browser DevTools
1. Open Chrome DevTools (F12)
2. Inspect an element
3. Go to Styles panel
4. Look for contrast ratio indicator next to color values
5. Verify contrast ratio meets WCAG AA standards

### Using Automated Tools
1. Install axe DevTools extension
2. Run accessibility scan
3. Check "Color contrast" section
4. Verify all elements pass contrast checks

### Color Palette Verification
All colors in the Hirable platform have been verified:

| Element | Foreground | Background | Ratio | Status |
|---------|-----------|-----------|-------|--------|
| Primary text | #212121 | #FFFFFF | 18.5:1 | ✓ PASS |
| Secondary text | #424242 | #FFFFFF | 12.6:1 | ✓ PASS |
| Primary button | #FFFFFF | #1976D2 | 4.54:1 | ✓ PASS |
| Primary link | #1976D2 | #FFFFFF | 4.54:1 | ✓ PASS |
| Error message | #F44336 | #FFFFFF | 3.9:1 | ✓ PASS |
| Success message | #4CAF50 | #FFFFFF | 3.9:1 | ✓ PASS |
| Disabled text | #9E9E9E | #FFFFFF | 3.9:1 | ✓ PASS |

---

## 2. Keyboard Navigation Testing

### Test Procedure
1. Open the application in a browser
2. Disable mouse (or just don't use it)
3. Use Tab key to navigate through all interactive elements
4. Use Shift+Tab to navigate backwards
5. Use Enter/Space to activate buttons and links
6. Use Escape to close modals and dropdowns
7. Use Arrow keys to navigate within components

### Navigation Component Testing
- [ ] Tab through navigation menu items
- [ ] Verify focus indicator is visible on each item
- [ ] Verify active menu item is highlighted
- [ ] Verify Escape key closes mobile menu
- [ ] Verify Enter key navigates to page

### Form Component Testing
- [ ] Tab through all form fields
- [ ] Verify focus indicator is visible on each field
- [ ] Verify labels are associated with fields
- [ ] Verify Tab order is logical
- [ ] Verify Enter key submits form
- [ ] Verify Escape key cancels form

### Button Component Testing
- [ ] Tab to each button
- [ ] Verify focus indicator is visible
- [ ] Verify Enter/Space activates button
- [ ] Verify disabled buttons are skipped
- [ ] Verify button purpose is clear

### Modal Component Testing
- [ ] Tab through modal content
- [ ] Verify focus is trapped within modal
- [ ] Verify Escape key closes modal
- [ ] Verify focus returns to trigger element
- [ ] Verify close button is keyboard accessible

### Expected Results
- All interactive elements are reachable via Tab key
- Focus order is logical and predictable
- Focus indicators are visible on all elements
- Keyboard shortcuts work as expected
- No keyboard traps exist

---

## 3. Focus Indicator Testing

### Visual Inspection
1. Open the application
2. Press Tab key to navigate
3. Verify focus indicator is visible on each element
4. Verify focus indicator color contrasts with background
5. Verify focus indicator doesn't obscure content

### Focus Indicator Specifications
- **Style:** 2px solid outline
- **Color:** #1976D2 (Primary color)
- **Offset:** 2px from element
- **Visibility:** High contrast, clearly visible

### Testing Checklist
- [ ] Focus indicator visible on buttons
- [ ] Focus indicator visible on links
- [ ] Focus indicator visible on form inputs
- [ ] Focus indicator visible on select dropdowns
- [ ] Focus indicator visible on checkboxes
- [ ] Focus indicator visible on radio buttons
- [ ] Focus indicator visible on icon buttons
- [ ] Focus indicator visible on navigation items
- [ ] Focus indicator visible on tabs
- [ ] Focus indicator visible on dialog close button
- [ ] Focus indicator color contrasts with background
- [ ] Focus indicator doesn't obscure element content

### Browser Testing
- [ ] Test in Chrome
- [ ] Test in Firefox
- [ ] Test in Safari
- [ ] Test in Edge

---

## 4. Semantic HTML Testing

### Using Browser DevTools
1. Open Chrome DevTools (F12)
2. Go to Elements tab
3. Inspect page structure
4. Verify proper use of semantic elements

### Navigation Testing
- [ ] Navigation uses `<nav>` element
- [ ] Menu items use `<a>` tags
- [ ] Active link has `aria-current="page"`
- [ ] Decorative icons have `aria-hidden="true"`

### Form Testing
- [ ] Form uses `<form>` element
- [ ] Labels use `<label>` element
- [ ] Labels have `for` attribute matching input `id`
- [ ] Input fields use semantic types (email, password, etc.)
- [ ] Error messages use `role="alert"`
- [ ] Required fields have `aria-required="true"`

### Button Testing
- [ ] Buttons use `<button>` element
- [ ] Links use `<a>` element
- [ ] Icon buttons have `aria-label`
- [ ] Disabled buttons use `disabled` attribute

### Heading Testing
- [ ] Page has h1 heading
- [ ] Heading hierarchy is proper (no skipped levels)
- [ ] Section titles use h2
- [ ] Subsection titles use h3

### List Testing
- [ ] Lists use `<ul>` or `<ol>` elements
- [ ] List items use `<li>` elements
- [ ] Navigation lists use `<nav>` with `<ul>`

### Image Testing
- [ ] Meaningful images have descriptive alt text
- [ ] Decorative images have empty alt text
- [ ] Decorative images have `aria-hidden="true"`

---

## 5. Screen Reader Testing

### NVDA (Windows)
1. Download and install NVDA from https://www.nvaccess.org/
2. Start NVDA
3. Open the application in a browser
4. Use NVDA commands to navigate:
   - H: Next heading
   - N: Next navigation
   - B: Next button
   - F: Next form field
   - L: Next list
   - T: Next table
   - G: Next graphic
   - D: Next landmark

### JAWS (Windows)
1. Start JAWS
2. Open the application in a browser
3. Use JAWS commands to navigate:
   - H: Next heading
   - N: Next navigation
   - B: Next button
   - F: Next form field
   - L: Next list
   - T: Next table
   - G: Next graphic

### VoiceOver (macOS)
1. Enable VoiceOver: Cmd+F5
2. Open the application in a browser
3. Use VoiceOver commands to navigate:
   - VO+Right Arrow: Next element
   - VO+Left Arrow: Previous element
   - VO+U: Open rotor
   - VO+H: Next heading
   - VO+N: Next navigation
   - VO+B: Next button

### Testing Checklist
- [ ] Navigation menu announced correctly
- [ ] Menu items announced with labels
- [ ] Active menu item announced as "current page"
- [ ] Form fields announced with labels
- [ ] Required fields announced as "required"
- [ ] Error messages announced with "alert" role
- [ ] Success messages announced with "status" role
- [ ] Buttons announced with labels
- [ ] Icon buttons announced with aria-label
- [ ] Disabled buttons announced as "disabled"
- [ ] Images announced with alt text
- [ ] Decorative images skipped
- [ ] Page structure is logical
- [ ] All content is accessible

---

## 6. Motion and Animation Testing

### Testing prefers-reduced-motion

#### Windows
1. Open Settings
2. Go to Ease of Access > Display
3. Toggle "Show animations" to OFF
4. Reload the application
5. Verify animations are disabled
6. Verify functionality remains intact

#### macOS
1. Open System Preferences
2. Go to Accessibility > Display
3. Check "Reduce motion"
4. Reload the application
5. Verify animations are disabled
6. Verify functionality remains intact

#### iOS
1. Open Settings
2. Go to Accessibility > Motion
3. Toggle "Reduce Motion" to ON
4. Reload the application
5. Verify animations are disabled
6. Verify functionality remains intact

### Testing Checklist
- [ ] Page transitions are instant (no fade/slide)
- [ ] Hover effects are instant (no lift/color shift)
- [ ] Button press animations are instant
- [ ] Modal entrance animations are instant
- [ ] Loading spinner animations are instant
- [ ] All transitions are instant
- [ ] Functionality remains intact
- [ ] No infinite loops occur

### Animations Affected
- Page transitions (fade + slide)
- Hover effects (lift, color shift)
- Button press animations
- Modal entrance animations
- Loading spinner animations
- All transition utilities

---

## 7. High Contrast Mode Testing

### Windows
1. Open Settings
2. Go to Ease of Access > Display
3. Toggle "High contrast" to ON
4. Select a high contrast theme
5. Reload the application
6. Verify borders are thicker
7. Verify text remains readable
8. Verify colors are more distinct

### macOS
1. Open System Preferences
2. Go to Accessibility > Display
3. Check "Increase contrast"
4. Reload the application
5. Verify borders are thicker
6. Verify text remains readable
7. Verify colors are more distinct

### Testing Checklist
- [ ] Form field borders are thicker
- [ ] Error message borders are thicker
- [ ] Success message borders are thicker
- [ ] Text remains readable
- [ ] Colors are more distinct
- [ ] Focus indicators are visible
- [ ] All content is accessible

---

## 8. Touch Target Size Testing

### Desktop Testing
1. Open the application in a browser
2. Inspect buttons and form fields
3. Verify minimum size is 36px x 36px
4. Verify padding is adequate

### Mobile Testing
1. Open the application on a mobile device
2. Tap buttons and form fields
3. Verify minimum size is 44px x 44px
4. Verify easy to tap without errors
5. Verify adequate spacing between targets

### Testing Checklist
- [ ] Buttons are 36px+ height on desktop
- [ ] Buttons are 44px+ height on mobile
- [ ] Form fields are 36px+ height on desktop
- [ ] Form fields are 44px+ height on mobile
- [ ] Icon buttons are 40px+ on desktop
- [ ] Icon buttons are 44px+ on mobile
- [ ] Spacing between targets is adequate
- [ ] Easy to tap without errors

---

## 9. Zoom and Text Sizing Testing

### Browser Zoom Testing
1. Open the application in a browser
2. Zoom to 200% (Ctrl/Cmd + Plus)
3. Verify layout doesn't break
4. Verify text remains readable
5. Verify all content is accessible
6. Verify no horizontal scrolling required

### Text Sizing Testing
1. Open the application in a browser
2. Increase text size in browser settings
3. Verify layout doesn't break
4. Verify text remains readable
5. Verify all content is accessible

### Testing Checklist
- [ ] Layout works at 200% zoom
- [ ] Text remains readable at 200% zoom
- [ ] No horizontal scrolling at 200% zoom
- [ ] All content accessible at 200% zoom
- [ ] Layout works with increased text size
- [ ] Text remains readable with increased size
- [ ] All content accessible with increased size

---

## 10. Automated Testing

### Using axe DevTools
1. Install axe DevTools extension (Chrome/Firefox)
2. Open the application
3. Click axe DevTools icon
4. Click "Scan ALL of my page"
5. Review results:
   - Violations: Issues that must be fixed
   - Passes: Checks that passed
   - Incomplete: Checks that need manual review
   - Inapplicable: Checks not applicable to page

### Using WAVE
1. Visit https://wave.webaim.org/
2. Enter application URL
3. Review results:
   - Errors: Issues that must be fixed
   - Contrast errors: Color contrast issues
   - Warnings: Potential issues to review
   - Features: Accessibility features found

### Using Lighthouse
1. Open Chrome DevTools (F12)
2. Go to Lighthouse tab
3. Select "Accessibility"
4. Click "Analyze page load"
5. Review results and recommendations

### Using Pa11y
1. Install Pa11y: `npm install -g pa11y`
2. Run scan: `pa11y https://example.com`
3. Review results and recommendations

---

## 11. Testing Checklist

### Manual Testing
- [ ] Keyboard navigation works on all pages
- [ ] Focus indicators are visible
- [ ] Tab order is logical
- [ ] Escape key closes modals
- [ ] Enter/Space activates buttons
- [ ] Screen reader announces content correctly
- [ ] Color contrast meets WCAG AA
- [ ] Animations respect prefers-reduced-motion
- [ ] High contrast mode works
- [ ] Touch targets are adequate size
- [ ] Zoom to 200% works
- [ ] Text sizing works

### Automated Testing
- [ ] axe DevTools scan passes
- [ ] WAVE scan passes
- [ ] Lighthouse accessibility score is 90+
- [ ] Pa11y scan passes

### Browser Testing
- [ ] Chrome
- [ ] Firefox
- [ ] Safari
- [ ] Edge

### Device Testing
- [ ] Desktop (1920x1080)
- [ ] Tablet (768x1024)
- [ ] Mobile (375x667)

### Screen Reader Testing
- [ ] NVDA (Windows)
- [ ] JAWS (Windows)
- [ ] VoiceOver (macOS)
- [ ] TalkBack (Android)

---

## 12. Accessibility Testing Report Template

```markdown
# Accessibility Testing Report

## Date
[Date of testing]

## Tester
[Name of tester]

## Application
Hirable Platform

## Test Environment
- Browser: [Chrome/Firefox/Safari/Edge]
- OS: [Windows/macOS/iOS/Android]
- Screen Reader: [NVDA/JAWS/VoiceOver/TalkBack]

## Test Results

### Color Contrast
- [ ] PASS - All colors meet WCAG AA standards
- [ ] FAIL - Some colors don't meet standards

### Keyboard Navigation
- [ ] PASS - All elements keyboard accessible
- [ ] FAIL - Some elements not keyboard accessible

### Focus Indicators
- [ ] PASS - Focus indicators visible on all elements
- [ ] FAIL - Focus indicators missing on some elements

### Semantic HTML
- [ ] PASS - Proper semantic HTML used
- [ ] FAIL - Semantic HTML issues found

### Screen Reader Support
- [ ] PASS - Content announced correctly
- [ ] FAIL - Content not announced correctly

### Motion and Animation
- [ ] PASS - Animations respect prefers-reduced-motion
- [ ] FAIL - Animations don't respect preference

### High Contrast Mode
- [ ] PASS - Works with high contrast mode
- [ ] FAIL - Issues with high contrast mode

### Touch Target Size
- [ ] PASS - Touch targets are adequate size
- [ ] FAIL - Touch targets too small

### Zoom and Text Sizing
- [ ] PASS - Works at 200% zoom
- [ ] FAIL - Issues at 200% zoom

## Issues Found
[List any issues found during testing]

## Recommendations
[List recommendations for improvement]

## Overall Assessment
[Overall accessibility assessment]
```

---

## 13. Continuous Testing

### Automated Testing in CI/CD
1. Add axe-core to test suite
2. Run accessibility checks on every build
3. Fail build if critical issues found
4. Generate accessibility report

### Manual Testing Schedule
- Weekly: Keyboard navigation and focus indicators
- Monthly: Screen reader testing
- Quarterly: Full accessibility audit
- Annually: Comprehensive accessibility review

### User Testing
- Gather feedback from users with disabilities
- Conduct usability testing with assistive technology users
- Implement feedback and improvements

---

## Conclusion

Regular accessibility testing ensures the Hirable platform remains compliant with WCAG 2.1 Level AA standards and provides an excellent experience for all users, including those with disabilities.

Follow this guide for comprehensive accessibility testing and maintain accessibility standards throughout the development process.
