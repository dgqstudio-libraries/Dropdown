# Dropdown

This is a custom component for Android to create content in a dropdown.

## Features

- Create amazing designs that can be shown/hidden.
- The dropdown supports any type of view.

## Requeriments

- API 24 or more.

## Setup dependencies

### Gradle

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
dependencies {
    implementation 'com.github.dgqstudio-libraries:Dropdown:1.0.0'
}
```

### Maven

```xml
<!-- <repositories> section of pom.xml -->
<repository>
    <id>jitpack.io</id>
   <url>https://jitpack.io</url>
</repository>

<!-- <dependencies> section of pom.xml -->
<dependency>
    <groupId>com.github.dgqstudio-libraries</groupId>
    <artifactId>Dropdown</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

```xml
<com.dgqstudio.dropdown.Dropdown
    android:id="@+id/dropdown"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:dropdownTitle="Title">

    <!-- HERE YOUR CONTENT -->

</com.dgqstudio.dropdown.Dropdown>
```

## Dropdown attributes

| Attribute                  | Format          | Default value |
|----------------------------|-----------------|---------------|
| dropdownCollapsedByDefault | boolean         | false         |
| dropdownTitle              | string          | ""            |
| dropdownTitleStyle         | reference       | 0             |
| dropdownButtonTint         | color/reference | null          |
| dropdownExpandedButtonSrc  | reference       | null          |
| dropdownCollapsedButtonSrc | reference       | null          |
| dropdownShowDivider        | boolean         | true          |
| headerPadding              | dimension       | 15f           |
| headerTopPadding           | dimension       | 0f            |
| headerStartPadding         | dimension       | 0f            |
| headerEndPadding           | dimension       | 0f            |
| headerBottomPadding        | dimension       | 0f            |
| headerBackgroundColor      | color/reference | null          |
| contentPadding             | dimension       | 15f           |
| contentTopPadding          | dimension       | 0f            |
| contentStartPadding        | dimension       | 0f            |
| contentEndPadding          | dimension       | 0f            |
| contentBottomPadding       | dimension       | 0f            |
| contentBackgroundColor     | color/reference | null          |
