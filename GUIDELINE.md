

# Coding Style Guide

This document are based on [Android Java Code Style](https://source.android.com/setup/contribute/code-style), [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) and other reference documents.

  * [Project guidelines](#project-guidelines)
    + [Package names](#package-names)
    + [Class names](#class-names)
    + [Resource files](#resource-files)
      - [Layout files](#layout-files)
      - [Values files](#values-files)
  * [Style guidelines](#style-guidelines)
    + [Always reformat your code](#always-reformat-your-code)
    + [Naming conventions](#naming-conventions)
    + [Use TODO comments](#use-todo-comments)
  * [Java language guidelines](#java-language-guidelines)
    + [Don't ignore exceptions](#dont-ignore-exceptions)
    + [Don't catch generic exceptions](#dont-catch-generic-exceptions)

## Project guidelines

### Package names

Package names are all lowercase, with consecutive words simply concatenated together (no underscores). For example,  `com.example.deepspace`, not  `com.example.deepSpace`  or  `com.example.deep_space`.

### Class names

Class names are written in [UpperCamelCase](https://google.github.io/styleguide/javaguide.html#s5.3-camel-case).  

For classes that extend an Android component, the name of the class should end with the name of the component; for example: `SignInActivity`, `SignInFragment`, `ImageUploaderService`, `ChangePasswordDialog`.

### Resource files

Resources file names are written in  **lowercase_underscore**.

See more info [here](https://github.com/ribot/android-guidelines/blob/master/project_and_code_guidelines.md#122-resources-files).

#### Layout files

Layout files should match the name of the Android components that they are intended for but moving the top level component name to the beginning. For example, if we are creating a layout for the `SignInActivity`, the name of the layout file should be `activity_sign_in.xml`.

| Component        | Class Name             | Layout Name                   |
| ---------------- | ---------------------- | ----------------------------- |
| Activity         | `UserProfileActivity`  | `activity_user_profile.xml`   |
| Fragment         | `SignUpFragment`       | `fragment_sign_up.xml`        |
| Dialog           | `ChangePasswordDialog` | `dialog_change_password.xml`  |
| AdapterView item | ---                    | `item_person.xml`             |
| Partial layout   | ---                    | `partial_stats_bar.xml`       |

A slightly different case is when we are creating a layout that is going to be inflated by an `Adapter`, e.g to populate a `ListView`. In this case, the name of the layout should start with `item_`.

Note that there are cases where these rules will not be possible to apply. For example, when creating layout files that are intended to be part of other layouts. In this case you should use the prefix `partial_`.

#### Values files

Resource files in the values folder should be  **plural**, e.g.  `strings.xml`,  `styles.xml`,  `colors.xml`,  `dimens.xml`,  `attrs.xml`.

## Style guidelines

### Always reformat your code

For Android Studio:
- Windows:  `Ctrl + Alt + L`.
- Linux:    `Ctrl + Shift + Alt + L`.
- macOS:    `Option + Command + L`.

See more info [here](https://stackoverflow.com/q/16580171/12405558).

### Naming conventions
-   **CamelCase** for classes.
-   **camelCase** for methods, local variables, and instance variables.
-   **lowercase_underscore** with prefix for resource IDs. Prefix should be abbreviated if possible.

Commonly used View classes:

| **Element** | **Prefix** |
|-------------|------------|
| `Button`    | `btn`      |
| `ImageView` | `img`      |
| `EditText`  | `input`    |
| `CheckBox`  | `chk`      |

-   Constants should be `ALL_CAPS_WITH_UNDERSCORES`.
-   Treat acronyms as words.

| Good           | Bad            |
| -------------- | -------------- |
| `XmlHttpRequest` | `XMLHTTPRequest` |
| `getCustomerId`  | `getCustomerID`  |
| `String url`     | `String URL`     |
| `long id`        | `long ID`        |

A good naming convention can be found [here](https://jeroenmols.com/blog/2016/03/07/resourcenaming), or the cheat sheet version [here](./.github/resourcenaming_cheatsheet.pdf)

### Use TODO comments
Use `TODO` comments for code that is temporary, a short-term solution, or good enough but not perfect. These comments should include the string `TODO` in all caps, followed by a colon:
```java
// TODO: Remove this code after the UrlTable2 has been checked in.  
```
and
```java
// TODO: Change this to use a flag instead of a constant.  
```
If your `TODO` is of the form "At a future date do something" make sure that you either include a specific date ("Fix by November 2005") or a specific event ("Remove this code after all production mixers understand protocol V7.").

## Java language guidelines

### Don't ignore exceptions

See why [here](https://source.android.com/setup/contribute/code-style#dont-ignore-exceptions). Acceptable alternatives (in order of preference) are:
- Throw the exception up to the caller of your method.
```java
void setServerPort(String value) throws NumberFormatException {
    serverPort = Integer.parseInt(value);
}
```
- Throw a new exception that's appropriate to your level of abstraction.
```java
void setServerPort(String value) throws ConfigurationException {
    try {
        serverPort = Integer.parseInt(value);
    } catch (NumberFormatException e) {
        throw new ConfigurationException("Port " + value + " is not valid.");
    }
}
```
- Substitute an appropriate value in the `catch {}` block.
```java
void setServerPort(String value) {
    try {
        serverPort = Integer.parseInt(value);
    } catch (NumberFormatException e) {
        serverPort = 80;  // default port for server 
    }
}
```
- Catch the exception and throw a new `RuntimeException`. This is dangerous, so do it only if you are positive that if this error occurs, the appropriate thing to do is crash.
```java
void setServerPort(String value) {
    try {
        serverPort = Integer.parseInt(value);
    } catch (NumberFormatException e) {
        throw new RuntimeException("port " + value " is invalid, ", e);
    }
}
```
- Last resort : You can ignore it if you are confident that everything will be fine, but you **must comment** an appropriate reason.
```java
void setServerPort(String value) {
    try {
        serverPort = Integer.parseInt(value);
    } catch (NumberFormatException e) {
        // Method is documented to just ignore invalid user input.
        // serverPort will just be unchanged.
    }
}
```

### Don't catch generic exceptions

:thumbsdown: **Not recommended**
```java
try {
    someComplicatedIOFunction();        // may throw IOException 
    someComplicatedParsingFunction();   // may throw ParsingException 
    someComplicatedSecurityFunction();  // may throw SecurityException 
    // phew, made it all the way 
} catch (Exception e) {                 // I'll just catch all exceptions 
    handleError();                      // with one generic handler!
}
```
See why [here](https://source.android.com/setup/contribute/code-style#dont-catch-generic-exception).

:thumbsup: **Recommended**
- Catch each exception separately as part of a multi-catch block, for example:
```java
try {
  ...  
} catch (ClassNotFoundException | NoSuchMethodException e) {
  ...  
}
```
- Refactor your code to have more fine-grained error handling, with multiple try blocks. Split up the IO from the parsing, and handle errors separately in each case.
- Rethrow the exception. Many times you don't need to catch the exception at this level anyway, just let the method throw it.
