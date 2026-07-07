# Library Management System (LBMS 4.0)

A JavaFX desktop application for managing a library's books, students, and borrow/return records, backed by a MySQL database. Originally built as a console application and converted into a full six-screen GUI.

## Features

- **Dashboard** — live stats (total books, total students, currently borrowed, most borrowed book, most active student)
- **Books** — view, add, and refresh the book catalog
- **Students** — view, add, and refresh registered students
- **Borrow** — select a student and an available book to check it out
- **Return** — view currently borrowed books, return one, and see any late fee due
- **Search** — look up books by title or ID, or students by name
- Auto-starts the MySQL80 Windows service on launch (requires admin elevation)
- Packaged as a standalone Windows `.exe` with a custom icon — no separate Java install required to run it

## Tech Stack

- Java 17
- JavaFX 21 (SDK for development, jmods for packaging)
- MySQL 8 (via `mysql-connector-j-9.7.0`)
- `jpackage` for building the standalone executable

## Project Structure

```
src/main/java/
  controllers/    JavaFX controllers (one per screen)
  dao/            Database access objects (CRUD + stats queries)
  models/         Plain data classes (Book, Student, BorrowRecord, etc.)
  services/       Library.java — business logic layer used by the console version
  utils/          SceneManager (navigation), DatabaseServiceManager (auto-start MySQL)
  Main.java       Original console-based entry point
  MainFX.java     JavaFX entry point

src/main/resources/
  fxml/           Screen layouts
  css/            Application styling

resources-jpackage/
  <AppName>.exe.manifest   Requests admin elevation on launch
  app-icon.ico             Application icon
```

## Running from Source (development)

1. Make sure you have:
   - JDK 17
   - JavaFX SDK 21 (for the classpath/module-path while developing)
   - MySQL running locally with a `library_db` database matching the schema used by the DAO classes
2. Open the project in VS Code with the Java extension.
3. Confirm `.vscode/settings.json` points at your JavaFX SDK's `lib` folder and lists `src/main/java` and `src/main/resources` as source paths.
4. Confirm `.vscode/launch.json` has a config named `MainFX` with `vmArgs` pointing at the same JavaFX SDK `lib` folder, e.g.:
   ```
   --module-path "<path-to-javafx-sdk>\lib" --add-modules javafx.controls,javafx.fxml
   ```
5. Run the `MainFX` launch configuration.

## Building the Standalone .exe

This produces a portable app folder containing the `.exe` and a bundled mini-JVM — no installer, just copy the folder anywhere.

**You will need, in addition to the JavaFX SDK above:**
- The **JavaFX jmods** bundle (separate download from the SDK, same Gluon page) — required because `jpackage`/`jlink` only pull in native graphics libraries correctly from `.jmod` files, not plain SDK jars.

### 1. Compile

From the project root:

```powershell
mkdir dist-classes
javac -encoding UTF-8 -d dist-classes --module-path "<path-to-javafx-sdk>\lib" --add-modules javafx.controls,javafx.fxml -cp "lib\mysql-connector-j-9.7.0.jar" (Get-ChildItem -Recurse -Path src\main\java -Filter *.java).FullName
```

> `-encoding UTF-8` is required — some source files contain emoji characters, and `javac` defaults to `windows-1252` on Windows otherwise.

### 2. Copy resources

```powershell
Copy-Item -Recurse src\main\resources\* dist-classes\ -Force
```

### 3. Build the JAR

```powershell
cd dist-classes
jar --create --file ../LBMS.jar --main-class MainFX .
cd ..
```

### 4. Stage a clean input folder for jpackage

```powershell
mkdir jpackage-input
Copy-Item LBMS.jar jpackage-input\
mkdir jpackage-input\lib
Copy-Item lib\mysql-connector-j-9.7.0.jar jpackage-input\lib\
```

> Don't point `jpackage`'s `--input` at the whole project root — it will bundle everything sitting there (source, build folders, etc). Use a dedicated, minimal folder instead.

### 5. Run jpackage

```powershell
jpackage --type app-image `
  --name "LibraryManagementSystem" `
  --input jpackage-input `
  --main-class MainFX `
  --main-jar LBMS.jar `
  --module-path "<path-to-javafx-jmods>" `
  --add-modules javafx.controls,javafx.fxml,java.sql,java.naming `
  --resource-dir resources-jpackage `
  --dest "<output-folder>" `
  --java-options "-Dprism.order=sw" `
  --icon "resources-jpackage\app-icon.ico"
```

**Notes on the flags that weren't obvious at first:**

- `--module-path` must point at the **jmods** folder for this step, not the SDK's `lib` folder — otherwise native graphics DLLs (`prism_d3d`, `glass`, etc.) go missing from the bundled runtime and the app fails with `Error initializing QuantumRenderer: no suitable pipeline found`.
- `java.sql` and `java.naming` must be added explicitly to `--add-modules`. The trimmed runtime `jpackage` builds only includes what's listed — without these, you'll get `NoClassDefFoundError: java/sql/DriverManager` and then `NoClassDefFoundError: javax/naming/NamingException` (the MySQL driver depends on both, even for a plain connection).
- `-Dprism.order=sw` forces JavaFX to use software rendering, which sidesteps graphics pipeline issues in some environments.
- `resources-jpackage/<AppName>.exe.manifest` must be named to match the app exactly (`LibraryManagementSystem.exe.manifest`). This is what makes Windows show the UAC prompt automatically on launch — the app runs elevated so it can start the MySQL Windows service without extra steps.
- Icon files converted from PNG occasionally end up double-extensioned (`app-icon.ico.ico`) if Explorer is hiding file extensions — check with `Get-ChildItem | Format-List Name, FullName` if `jpackage` reports the icon "does not exist" despite it clearly being there.

### 6. Run it

The finished app lives at:
```
<output-folder>\LibraryManagementSystem\LibraryManagementSystem.exe
```

Double-clicking it should prompt for admin elevation, auto-start the MySQL80 service if it isn't already running, and open straight into the dashboard.

## Database

Requires a MySQL database named `library_db` with tables: `books`, `students`, `borrowed_books`. Connection details (URL, user, password) are currently hardcoded in each DAO class — update these to match your own local MySQL setup if cloning this project.

The JDBC URL includes `useSSL=false&allowPublicKeyRetrieval=true` — required for the packaged `.exe`'s trimmed runtime, which otherwise fails the SSL handshake against a local, non-SSL MySQL instance.

## Known Limitations / Possible Next Steps

- Database credentials are hardcoded rather than externalized to a config file
- No installer (.msi) — currently a portable app-image only, by choice, since this isn't distributed at scale
- "Most borrowed book" / "most active student" stats count all-time borrow history, not just currently active borrows
