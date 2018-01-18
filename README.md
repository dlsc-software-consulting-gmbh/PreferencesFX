# PreferencesFX
**Preferences dialogs for business applications made easy. Creating preference dialogs in Java has never been this easy!**

## What is PreferencesFX?

Creating preference dialogs in JavaFX is a tedious and very error-prone task. PreferencesFX is a framework which solves this problem. It enables the developer to create preferences dialogs with ease and creates well-designed and user-friendly preference dialogs by default. 

## Main Features

- Simple and understandable  API
- The most important Features are shown in the picture below: The created preferences dialog
    - Search / Filter function
    - Tree view which shows all categories the user defines
    - BreadCrumbBar which shows the user the path to the selected category and allows him to navigate back
    - Undo / Redo Buttons which are allowing the user a stepwise undo and redo possibility of his last changes.
    - Instant persistance of any changes to the application
    - Various setting types are supported
- Regarding the setting types we're also referencing to the requirements.adoc. You can create one when launching the gradle task: 'ascidoctor'. The most important settings are shown further in the document.

![alt text](docs/images/preferencesFX_in_use.png) The created preferences dialog

## Semantics

PreferencesFX contains different semantic layers. A preferences dialog can contain multiple Categories. Each `Category` can contain multiple Groups and each `Group`can contain multiple `Settings`.

For better illustration, the basic concept of writing a dialog is shown below:
```Java
PreferencesFx.of(
        Category.of("Category Title",
                Group.of("Group Title",
                          Setting.of("Setting Title", new Property())
                )
        )
);
```

Note: When the user of the API decides to only add   

FormsFX offers different semantic layers. The largest entity is the form. It contains groups and sections, which in turn act as containers for fields. Fields are the end user's primary point of interaction as they handle data input and presentation.

## Defining a form

Creating a form is as simple as calling `Form.of()`.

```Java
Form.of(
        Group.of(
                Field.ofStringType("")
                        .label("Username"),
                Field.ofStringType("")
                        .label("Password")
                        .required("This field can’t be empty")
        ),
        Group.of(…)
).title("Login");
```

Fields have a range of options that define their semantics and change their functionality.

Option | Description
------ | -----------
`label(String)` | Describes the field’s content in a concise manner. This description is always visible and usually placed next to the editable control.
`tooltip(String)` | This contextual hint further describes the field. It is usually displayed on hover or focus.
`placeholder(String)` | This hint describes the expected input as long as the field is empty.
`required(boolean)` <br /> `required(String)` | Determines, whether entry in this field is required for the correctness of the form.
`editable(boolean)` | Determines, whether end users can edit the contents of this field.
`id(String)` | Describes the field with a unique ID. This is not visible directly, but can be used for styling purposes.
`styleClass(List&lt;String&gt;)` | Adds styling hooks to the field. This can be used on the view layer.
`span(int)` <br /> `span(ColSpan)` | Determines, how many columns the field should span on the view layer. Can be a number between 1 and 12 or a ColSpan fraction.
`render(SimpleControl)` | Determines the control that is used to render this field on the view layer.


The following table shows how to create different fields and how they look by default:

String Control

<table>
  <tbody>
    <tr>
      <td colspan="2">String Control</td>
    </tr>
    <tr>
      <td><img src="./docs/images/StringField.png" /></td>
    </tr>
    <tr>
      <td>
        <pre lang="java">Field.ofStringType("CHF")
     .label("Currency")</pre>
      </td>
    </tr>
  </tbody>
</table>
<table>
  <tbody>
    <tr>
      <td colspan="2">Integer Control</td>
    </tr>
    <tr>
      <td><img src="./docs/images/IntegerField.png" /></td>
    </tr>
    <tr>
      <td>
        <pre lang="java">Field.ofIntegerType(8401120)
     .label("Population")</pre>
      </td>
    </tr>
  </tbody>
</table>
<table>
  <tbody>
    <tr>
      <td colspan="2">Double Control</td>
    </tr>
    <tr>
      <td><img src="./docs/images/DoubleField.png" /></td>
    </tr>
    <tr>
      <td>
        <pre lang="java">Field.ofDoubleType(41285.0)
       .label("Area")</pre>
      </td>
    </tr>
  </tbody>
</table>
<table>
  <tbody>
    <tr>
      <td colspan="2">Boolean Control</td>
    </tr>
    <tr>
      <td><img src="./docs/images/BooleanField.png" /></td>
    </tr>
    <tr>
      <td>
        <pre lang="java">Field.ofBooleanType(false)
     .label("Independent")</pre>
      </td>
    </tr>
  </tbody>
</table>
<table>
  <tbody>
    <tr>
      <td colspan="2">ComboBox Control</td>
    </tr>
    <tr>
      <td><img src="./docs/images/ComboBoxField.png" /></td>
    </tr>
    <tr>
      <td>
        <pre lang="java">Field.ofSingleSelectionType(Arrays.asList("Zürich (ZH)", "Bern (BE)", …), 1)
     .label("Capital")</pre>
      </td>
    </tr>
  </tbody>
</table>
<table>
  <tbody>
    <tr>
      <td colspan="2">RadioButton Control</td>
    </tr>
    <tr>
      <td><img src="./docs/images/RadioButtonField.png" /></td>
    </tr>
    <tr>
      <td>
        <pre lang="java">Field.ofSingleSelectionType(Arrays.asList("Right", "Left"), 0)
     .label("Driving on the")
     .render(new SimpleRadioButtonControl<>())</pre>
      </td>
    </tr>
  </tbody>
</table>
<table>
  <tbody>
    <tr>
      <td colspan="2">CheckBox Control</td>
    </tr>
    <tr>
      <td><img src="./docs/images/CheckBoxField.png" /></td>
    </tr>
    <tr>
      <td>
        <pre lang="java">Field.ofMultiSelectionType(Arrays.asList("Africa", "Asia", …), Collections.singletonList(2))
     .label("Continent")
     .render(new SimpleCheckBoxControl<>())</pre>
      </td>
    </tr>
  </tbody>
</table>
<table>
  <tbody>
    <tr>
      <td colspan="2">ListView Control</td>
    </tr>
    <tr>
      <td><img src="./docs/images/ListField.png" /></td>
    </tr>
    <tr>
      <td>
        <pre lang="java">Field.ofMultiSelectionType(Arrays.asList("Zürich (ZH)", "Bern (BE)", …), Arrays.asList(0, 1, …))
     .label("Biggest Cities")</pre>
      </td>
    </tr>
  </tbody>
</table>

## Rendering a form

The only point of interaction is the `FormRenderer`. It delegates rendering of further components to other renderers.

```java
Pane root = new Pane();
root.getChildren().add(new FormRenderer(form));
```

All fields have a default control that is used for rendering. This can be changed to another compatible implementation using the `render()` method.

```java
Field.ofMultiSelectionType(…)
        .render(new SimpleCheckBoxControl<>())
```

## Model

Forms are used to create and manipulate data. In order to use this data in other parts of an application, model classes can be used. These classes contain properties, which are then bound to the persisted value of a field.

```java
StringProperty name = new SimpleStringProperty("Hans");
Field.ofStringType(name);
```

The `persist()` and `reset()` methods can be used to store and revert field values, which in turn updates the binding.

Fields in FormsFX store their values in multiple steps. For free-form fields, like `StringField` or `DoubleField`, the exact user input is stored, along with a type-transformed value and a persistent value. The persistence is, by default, handled manually, but this can be overridden by setting the `BindingMode` to `CONTINUOUS` on the form level.

## Localisation

All displayed values are localisable. Methods like `label()`, `placeholder()` accept keys which are then used for translation. By default, FormsFX includes a `ResourceBundle`-based implementation, however, this can be exchanged for a custom implementation.

```java
private ResourceBundle rbDE = ResourceBundle.getBundle("demo.demo-locale", new Locale("de", "CH"));
private ResourceBundle rbEN = ResourceBundle.getBundle("demo.demo-locale", new Locale("en", "UK"));

private ResourceBundleService rbs = new ResourceBundleService(rbEN);

Form.of(…)
        .i18n(rbs);
```

## Validation

All fields are validated whenever end users edit the contained data. FormsFX offers a wide range of pre-defined validators, but also includes support for custom validators using the `CustomValidator.forPredicate()` method.

| Validator | Description |
| --------- | ----------- |
| `CustomValidator` | Define a predicate that returns whether the field is valid or not. |
| `DoubleRangeValidator` | Define a number range which is considered valid. This range can be limited in either one direction or in both directions. |
| `IntegerRangeValidator` | Define a number range which is considered valid. This range can be limited in either one direction or in both directions. |
| `RegexValidator` | Valiate text against a regular expression. This validator offers pre-defined expressions for common use cases, such as email addresses.
| `SelectionLengthValidator` | Define a length interval which is considered valid. This range can be limited in either one direction or in both directions. |
| `StringLengthValidator` | Define a length interval which is considered valid. This range can be limited in either one direction or in both directions. |

## Advantages

- Less error-prone
- Less code needed
- Easy to learn
- Easy to understand
- Easy to extend

# Documentation

- [Javadocs](http://dlsc.com/wp-content/html/formsfx/apidocs/)
- [Report](./docs/Project%20Report.pdf)





# PreferencesFX
**Preferences dialogs for business applications made easy. Creating preference dialogs in Java has never been this easy!**

## What is PreferencesFX?

Creating preference dialogs in JavaFX is a tedious and very error-prone task. PreferencesFX is a framework which solves this problem. 
It enables the developer to create preferences dialogs with ease and creates well-designed and user-friendly preference dialogs by default. 

## Main Features

- Simple and understandable  API
- The most important Features are shown below in the MVP.png
- We're also referencing to the requirements.adoc. You can create one when launching the gradle task: 'ascidoctor'

![alt text](MVP.png) MVP.png

##Team
 
- Marco Sanfratello
  - marco.sanfratello@students.fhnw.ch
  - Skype: sanfratello.m@gmail.com 
  - GitHub: Genron

- François Martin
  - francois.martin@students.fhnw.ch 
  - Skype: francoisamimartin
  - GitHub: martinfrancois
  
- Dirk Lemmermann
  - dlemmermann@gmail.com
  - Skype: dlemmermann
  - GitHub: dlemmermann
  
- Dieter Holz
  - dieter.holz@fhnw.ch
  - Skype: dieter.holz.canoo.com
  - GitHub: DieterHolz

