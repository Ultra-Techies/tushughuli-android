
# Tushughuli
An android application that allows users to note down their to-do lists in an organized structure. This will help the users to remain focused on their tasks and increase their productivity.

# Table Of Contents


* [About the App](#AboutTheApp)

* [General Preview](#general-preview)

* [Architecture](#architecture)

* [Permissions](#permissions)

* [Run Poject](#run-locally)

* [Installation](#installation)

* [Dependencies](#dependencies)

* [To Do List](#todo)

* [Authors](#todo)

* [Contributing](#contributing)

* [Wiki](#wiki)

* [License](#license)
## About the App
The app has the following key features that a user can do:
- Sign up and login
- Create tasks
- Create reminders to tasks
- View tasks that are in progress
- View tasks that are done
- View profile
## General Preview

<a href="url"><img src=https://raw.githubusercontent.com/Ultra-Techies/Todolist-android/main/design/Todolist%20Wire%20frame%20-%20Splash%20Screen.jpg height="550"  ></a>
<a href="url"><img src=https://raw.githubusercontent.com/Ultra-Techies/Todolist-android/main/design/Todolist%20Wire%20frame%20-%20Login.jpg height="550"  ></a>
<a href="url"><img src=https://raw.githubusercontent.com/Ultra-Techies/Todolist-android/main/design/Todolist%20Wire%20frame%20-%20Sign%20up.jpg height="550"  ></a>
<a href="url"><img src=https://raw.githubusercontent.com/Ultra-Techies/Todolist-android/main/design/Todolist%20Wire%20frame%20-%20Main%20Screen.jpg height="550"  ></a>
<a href="url"><img src=https://raw.githubusercontent.com/Ultra-Techies/Todolist-android/main/design/Todolist%20Wire%20frame%20-%20Profile.jpg height="550"  ></a>
<a href="url"><img src=https://raw.githubusercontent.com/Ultra-Techies/Todolist-android/main/design/Todolist%20Wire%20frame%20-%20Settings%20Screen.jpg height="550"  ></a>
<a href="url"><img src=https://raw.githubusercontent.com/Ultra-Techies/Todolist-android/main/design/Todolist%20Wire%20frame%20-%20Add%20new%20task.jpg height="550"  ></a>
<a href="url"><img src=https://raw.githubusercontent.com/Ultra-Techies/Todolist-android/main/design/Todolist%20Wire%20frame%20-%20Add%20new%20task.jpg height="550"  ></a>

## Architecture
The app uses the MVVM architecture
The app directory has two packages: ui and data.

### Data Package:
The data package has the following sub packages:

#### Network
It holds the classes that make the http network calls to the api endpoints

#### Requests
It contains requests that are sent to the backend.

#### Responses
It contains responses received from the network calls

#### Repository
It defines the http endpoints that need to be executed and exposes data received from the api to the ui layer.

#### Di (Dependency Injection)
It holds the app module class where we use hilt to inject our classes

### UI Package:
This layer has  the following sub packages:

#### Splash Activity
Entry screen for the user, it checks for internet connection before proceeding to the next activity

#### Auth
It has the registration and login features. Each feature has a viewmodel and a fragment.

#### Home
It has the following features:
- Main Activity- that displays tasks
- Bottomsheet - that allows a user to add tasks and edit tasks

#### Profile
It holds the users Information such as:
- Username
- Email

#### Settings
It allows a user to turn on notifications and change their information such as email and passwords.

#### Utils
All shared classes are found in the utils.

#### App
Entry point of our application

<a href="url"><img src= height="550"  ></a>

## Permissions

You need to allow the following permissions before running the app:

- Internet
## Run Project
You can check out the compiled version of the app here: [appetize](https://appetize.io/#howitworks)
## Installations

Clone the project

```bash
  git clone https://github.com/Ultra-Techies/tushughuli-android
```

Go to android studio compile and install dependencies

Run the app on your emulator or device



## Dependencies

- Retrofit/Gson

- Picasso

- HILT

- Coroutines

- view binding

- Live Data

- Jetpack DataStore

- Mockito

## To Do List

- [ ] Design Screens
- [ ] Implement the screens designs
- [ ] Consume Api and populate tasks lists.
- [ ] Unit Testing
- [ ] Deploy app on [appetize.io](https://appetize.io/#howitworks)

## Contributers
Auto-populated from:
[contributors-img](https://contributors-img.firebaseapp.com/image?repo=Ultra-Techies/Todolist-android)

<a href="https://github.com/Ultra-Techies/Todolist-android/graphs/contributors">
  <img src="https://contributors-img.firebaseapp.com/image?repo=Ultra-Techies/Todolist-android" />
</a>

## Contributing

Contributions are always welcome!

See [contributing guidelines](https://github.com/github/docs/blob/main/CONTRIBUTING.md) for ways to get started.

Please adhere to this project's `code of conduct`.

1. Fork it
2. Create your feature branch (git checkout -b my-new-feature)
3. Commit your changes (git commit -m 'Add some feature')
4. Push your branch (git push origin my-new-feature)
5. Create a new Pull Request

## Wiki

Check out the app's wiki [here](https://github.com/Ultra-Techies/Todolist-android/wiki)


## License

[MIT](https://choosealicense.com/licenses/mit/)
