# Contribution Guide
Thank you for considering contributing to the **QTI to Learnosity Converter** project! Contributions are what makes open-source projects thrive, and we are excited to collaborate with you.

## How to Contribute

### Report Issues
If you find a bug, have questions, or want to suggest an improvement:
- Check the [Issues](https://github.com/epam/qti-learnosity/issues) page to see if it has already been reported.
- If not, create a new issue with a clear description, including steps to reproduce the problem (if applicable).

### Submit Enhancements or Fixes
We welcome contributions of all sizes, from minor typo fixes to significant new features.

#### Before You Start:
- Ensure your work aligns with the project’s goals and scope.
- For significant changes, open an issue to discuss your proposal with maintainers before starting.

#### Development Workflow:
1. **Fork the Repository**\
    Click the "Fork" button at the top-right corner of this repository.

2. **Clone Your Fork**\
    Create a local copy of your forked repository on your computer.

3. **Create a Feature Branch**\
    To maintain consistency and clarity in the repository, please use the following branch naming convention:
    ```
    <prefix>/issue-<issue#>/<branch name>
    ```

    **\<prefix\>** should indicate the overall intent of the code (a given issue) in the branch.
    - Possible prefix values: chore, feature, fix, refactor, docs
    - A prefix must always start with a lowercase letter

    **\<branch name>** should concisely describe the code (a given issue) in the branch.
    - Each word in a branch name must be separated by a hyphen / minus sign (-)
    - Branch names must start with a lowercase letter

    Examples:
    ```
    feature/issue-11/logging
    feature/issue-8/text-entry
    fix/issue-12/missing-newlines
    ```

4. **Implement Your Changes**
    - Follow the project's coding style and guidelines.
    - Include clear, concise comments where necessary.
    - Update documentation if your changes affect it.

5. **Run Tests**\
    Make sure your changes do not break any existing functionality. Run the test suite with `./gradlew clean test`
    If applicable, add new tests for your changes.

6. **Commit Your Changes**
    - Write clear, descriptive commit messages that follow the [Conventional Commits v1.0.0](https://www.conventionalcommits.org/en/v1.0.0/) convention.
    - Ensure each commit focuses on a single change or feature.

7. **Push Your Branch**\
Push your feature branch to your forked repository:

8. **Create a Pull Request**
    - Go to your forked repository on GitHub and navigate to the branch you just pushed.
    - Click the "Compare & pull request" button.
    - Provide a detailed description of your changes and reference any related issues.

Thank you for contributing! Your efforts help make this project better for everyone.
