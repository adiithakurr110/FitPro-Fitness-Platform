## Description
Please include a summary of the changes and the related issue/feature.

- Feature / Fix:
- Components impacted: [Servlets | JSP Views | DAOs | Swing GUI | Services | Database]

## Type of Change
- [ ] New feature (non-breaking change adding functionality)
- [ ] Bug fix (non-breaking change fixing an issue)
- [ ] Documentation update
- [ ] Quality Assurance / Test Suite addition

## Quality Assurance & Verification Checklist
- [ ] My code adheres to the project's coding standards.
- [ ] I have executed `mvn test` locally and all 32+ tests pass with zero errors.
- [ ] All database queries utilize parameterized `PreparedStatement` to prevent SQL injection.
- [ ] All dynamic JSP outputs utilize `<c:out>` for XSS defense.
- [ ] Code is self-documented with JavaDoc comments for non-trivial logic.
- [ ] Peer review conducted.
