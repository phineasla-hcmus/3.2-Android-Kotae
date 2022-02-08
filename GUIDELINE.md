<!-- omit in toc -->
# Coding Style Guide

- [Naming conventions](#naming-conventions)
  - [Package names](#package-names)
  - [Class names](#class-names)
  - [Field names](#field-names)
  - [Resource names](#resource-names)
    - [Drawable files](#drawable-files)
    - [Layout files](#layout-files)
    - [Values files](#values-files)
    - [Prefix your strings.xml](#prefix-your-stringsxml)
    - [Use colors.xml and dimens.xml as a pallete](#use-colorsxml-and-dimensxml-as-a-pallete)
- [Split a large style file into other files](#split-a-large-style-file-into-other-files)
- [Always reformat your code](#always-reformat-your-code)
- [Use TODO comments](#use-todo-comments)
- [Java language guidelines](#java-language-guidelines)
  - [Don't ignore exceptions](#dont-ignore-exceptions)
  - [Don't catch generic exceptions](#dont-catch-generic-exceptions)

---

This document are based on:

-   [Android Java Code Style](https://source.android.com/setup/contribute/code-style)
-   [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
-   [ribot/android-guidelines](https://github.com/ribot/android-guidelines/blob/master/project_and_code_guidelines.md)
-   [futurice/android-best-practices](https://github.com/futurice/android-best-practices)
-   [Naming convention - Jeroen Mols](https://jeroenmols.com/blog/2016/03/07/resourcenaming)

## Naming conventions

### Package names

Package names are all lowercase, with consecutive words simply concatenated together (no underscores). For example, `com.example.deepspace`, not `com.example.deepSpace` or `com.example.deep_space`.

### Class names

Class names are written in [UpperCamelCase](https://google.github.io/styleguide/javaguide.html#s5.3-camel-case).

For classes that extend an Android component, the name of the class should end with the name of the component; for example: `SignInActivity`, `SignInFragment`, `ImageUploaderService`, `ChangePasswordDialog`.

### Field names

-   Constants should be `ALL_CAPS_WITH_UNDERSCORES`.
-   Treat acronyms as words.

| Good             | Bad              |
| ---------------- | ---------------- |
| `XmlHttpRequest` | `XMLHTTPRequest` |
| `getCustomerId`  | `getCustomerID`  |
| `String url`     | `String URL`     |
| `long id`        | `long ID`        |

### Resource names

Resources file names are written in **lowercase_underscore**.

Resource IDs should be **lowercase_underscore** with prefix. Prefix should be abbreviated if possible.

Commonly used View classes:

| **Element**      | **Prefix**       |
| ---------------- | ---------------- |
| `Button`         | `btn`            |
| `ImageView`      | `img`            |
| `EditText`       | `et`             |
| `TextView`       | `tv`             |
| `CheckBox`       | `chk`            |
| `RadioButton`    | `rb`             |
| `ToggleButton`   | `tb`             |
| `Menu`           | `menu`           |
| `LinearLayout`   | `linearlayout`   |
| `RelativeLayout` | `relativelayout` |

#### Drawable files

Some of these namings already hinted by Android Studio, so I just leave it here for reference.

<details>
<summary>Naming conventions for drawables</summary>

| Asset Type   | Prefix          | Example                    |
| ------------ | --------------- | -------------------------- |
| Action bar   | `ab_`           | `ab_stacked.9.png`         |
| Button       | `btn_`          | `btn_send_pressed.9.png`   |
| Dialog       | `dialog_`       | `dialog_top.9.png`         |
| Divider      | `divider_`      | `divider_horizontal.9.png` |
| Icon         | `ic_`           | `ic_star.png`              |
| Menu         | `menu_	`        | `menu_submenu_bg.9.png`    |
| Notification | `notification_` | `notification_bg.9.png`    |
| Tabs         | `tab_`          | `tab_pressed.9.png`        |
</details>

<details>
<summary>Naming conventions for icons</summary>

| Asset Type                      | Prefix           | Example                    |
| ------------------------------- | ---------------- | -------------------------- |
| Icons                           | `ic_`            | `ic_star.png`              |
| Launcher icons                  | `ic_launcher`    | `ic_launcher_calendar.png` |
| Menu icons and Action Bar icons | `ic_menu`        | `ic_menu_archive.png`      |
| Status bar icons                | `ic_stat_notify` | `ic_stat_notify_msg.png`   |
| Tab icons                       | `ic_tab`         | `ic_tab_recent.png`        |
| Dialog icons                    | `ic_dialog`      | `ic_dialog_info.png`       |
</details>

<details>
<summary>Naming conventions for selector states</summary>

| State    | Suffix      | Example                    |
| -------- | ----------- | -------------------------- |
| Normal   | `_normal`   | `btn_order_normal.9.png`   |
| Pressed  | `_pressed`  | `btn_order_pressed.9.png`  |
| Focused  | `_focused`  | `btn_order_focused.9.png`  |
| Disabled | `_disabled` | `btn_order_disabled.9.png` |
| Selected | `_selected` | `btn_order_selected.9.png` |
</details>

#### Layout files

Layout files should match the name of the Android components that they are intended for but moving the top level component name to the beginning. For example, if we are creating a layout for the `SignInActivity`, the name of the layout file should be `activity_sign_in.xml`.

| Component        | Class Name             | Layout Name                  |
| ---------------- | ---------------------- | ---------------------------- |
| Activity         | `UserProfileActivity`  | `activity_user_profile.xml`  |
| Fragment         | `SignUpFragment`       | `fragment_sign_up.xml`       |
| Dialog           | `ChangePasswordDialog` | `dialog_change_password.xml` |
| AdapterView item | ---                    | `item_person.xml`            |
| Partial layout   | ---                    | `partial_stats_bar.xml`      |

A slightly different case is when we are creating a layout that is going to be inflated by an `Adapter`, e.g to populate a `ListView`. In this case, the name of the layout should start with `item_`.

Note that there are cases where these rules will not be possible to apply. For example, when creating layout files that are intended to be part of other layouts. In this case you should use the prefix `partial_`.

#### Values files

Resource files in the values folder should be **plural**, e.g. `strings.xml`, `styles.xml`, `colors.xml`, `dimens.xml`, `attrs.xml`.

#### Prefix your strings.xml

:thumbsdown: **Not recommended**

```xml
<string name="network_error">Network error</string>
<string name="call_failed">Call failed</string>
<string name="map_failed">Map loading failed</string>
```

:thumbsup: **Recommended**

```xml
<string name="error_message_network">Network error</string>
<string name="error_message_call">Call failed</string>
<string name="error_message_map">Map loading failed</string>
```

#### Use colors.xml and dimens.xml as a pallete

:thumbsdown: **Not recommended**

```xml
<resources>
    <color name="button_foreground">#FFFFFF</color>
    <color name="button_background">#2A91BD</color>
</resources>    
```

:thumbsup: **Recommended**

```xml
<resources>
    <!-- grayscale -->
    <color name="white">#FFFFFF</color>
    <!-- basic colors -->
    <color name="blue">#2A91BD</color>
</resources>
```

The names do not need to be plain color names as "green", "blue", etc. Names such as "brand_primary", "brand_secondary", "brand_negative" are totally acceptable as well.

By referencing the color palette from your styles allows you to abstract the underlying colors from their usage in the app, as per:

- `colors.xml` - defines only the color palette.
- `styles.xml` - defines styles which reference the color palette and reflects the color usage. (e.g. the button foreground is white).
- `activity_main.xml` - references the appropriate style in `styles.xml` to color the button.

## Split a large style file into other files

You don't need to have a single `styles.xml` file. Android SDK supports other files out of the box, there is nothing magical about the name `styles`, what matters are the XML tags `<style>` inside the file. Hence you can have files `styles.xml`, `styles_home.xml`, `styles_item_details.xml`, `styles_forms.xml`. Unlike resource directory names which carry some meaning for the build system, filenames in `res/values` can be arbitrary.

## Always reformat your code

For Android Studio:
-   Reformat code:
    -   Windows: `Ctrl + Alt + L`.
    -   Linux: `Ctrl + Shift + Alt + L`.
    -   macOS: `Option + Command + L`.
-   Optimize imports:
    -   Windows: `Ctrl + Alt + O`.
    -   macOS: `Option + Command + O`.

See more info [here](https://stackoverflow.com/q/16580171/12405558). If you perfer Visual Studio Code shortcuts then you can see [here](https://stackoverflow.com/a/64284560/12405558).

## Use TODO comments

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

<details>
  <summary><i>Why</i></summary>
  While you may think your code will never encounter this error condition or that it isn't important to handle it, ignoring this type of exception creates mines in your code for someone else to trigger some day. You must handle every exception in your code in a principled way; the specific handling varies depending on the case.
	
  > "Anytime somebody has an empty catch clause they should have a creepy feeling. There are definitely times when it is actually the correct thing to do, but at least you have to think about it. In Java you can't escape the creepy feeling." — [James Gosling](http://www.artima.com/intv/solid4.html)
</details>

Acceptable alternatives (in order of preference) are:

-   Throw the exception up to the caller of your method.

```java
void setServerPort(String value) throws NumberFormatException {
    serverPort = Integer.parseInt(value);
}
```

-   Throw a new exception that's appropriate to your level of abstraction.

```java
void setServerPort(String value) throws ConfigurationException {
    try {
        serverPort = Integer.parseInt(value);
    } catch (NumberFormatException e) {
        throw new ConfigurationException("Port " + value + " is not valid.");
    }
}
```

-   Substitute an appropriate value in the `catch {}` block.

```java
void setServerPort(String value) {
    try {
        serverPort = Integer.parseInt(value);
    } catch (NumberFormatException e) {
        serverPort = 80;  // default port for server
    }
}
```

-   Catch the exception and throw a new `RuntimeException`. This is dangerous, so do it only if you are positive that if this error occurs, the appropriate thing to do is crash.

```java
void setServerPort(String value) {
    try {
        serverPort = Integer.parseInt(value);
    } catch (NumberFormatException e) {
        throw new RuntimeException("port " + value " is invalid, ", e);
    }
}
```

-   Last resort : You can ignore it if you are confident that everything will be fine, but you **must comment** an appropriate reason.

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

<details>
  <summary><i>Why</i></summary>

  In almost all cases, it's inappropriate to catch generic `Exception` or `Throwable` (preferably not `Throwable` because it includes `Error` exceptions). It's dangerous because it means that exceptions you never expected (including runtime exceptions like `ClassCastException`) get caught in app-level error handling. It obscures the failure handling properties of your code, meaning if someone adds a new type of exception in the code you're calling, the compiler won't point out that you need to handle the error differently. In most cases you shouldn't be handling different types of exceptions in the same way.

  The rare exception to this rule is test code and top-level code where you want to catch all kinds of errors (to prevent them from showing up in a UI, or to keep a batch job running). In these cases, you may catch generic `Exception` (or `Throwable`) and handle the error appropriately. Think carefully before doing this, though, and put in comments explaining why it's safe in this context.
</details>

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

:thumbsup: **Recommended**

-   Catch each exception separately as part of a multi-catch block, for example:

```java
try {
  ...
} catch (ClassNotFoundException | NoSuchMethodException e) {
  ...
}
```

-   Refactor your code to have more fine-grained error handling, with multiple try blocks. Split up the IO from the parsing, and handle errors separately in each case.
-   Rethrow the exception. Many times you don't need to catch the exception at this level anyway, just let the method throw it.
