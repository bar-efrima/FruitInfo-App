# Fruit Info App

A mobile application that displays information about various fruits using the [FruityVice API](https://www.fruityvice.com). The app allows users to sort fruit data by sugar, protein, and calories, and also features integration with the AppsFlyer SDK for tracking user interactions and conversion data.

## Features

- Fetches fruit data from the **FruityVice API** and displays it in a scrollable list.
- Sorts the fruit list by **sugar**, **protein**, or **calories** content.
- Provides a **reset** button to restore the list to its initial state.
- Tracks button click events ("Sugar", "Protein", "Calories", and "Reset") using **AppsFlyer SDK**.
- Conversion Data Listener integrated for tracking non-organic installs.
- Simple, intuitive UI for easy browsing and sorting of fruit information.

## Screenshots

![Main Screen](./screenshots/main_screen.png)
![Sorted by Sugar](./screenshots/sorted_by_sugar.png)

## API

This app uses the [FruityVice API](https://www.fruityvice.com) to fetch data about fruits such as:

- Fruit name
- Sugar content
- Protein content
- Calories

## How to Use

1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/fruit-info-app.git
    ```

2. Install the required dependencies and SDKs for Android or iOS development.
3. Run the app on an Android or iOS device/emulator.

### Sorting

- Tap the **Sugar**, **Protein**, or **Calories** buttons to sort the fruits by the selected metric.
- Use the **Reset** button to restore the original unsorted list.

## AppsFlyer SDK Integration

This app includes **AppsFlyer SDK** integration to track user interactions:

- **Button Clicks**: Every button click ("Sugar", "Protein", "Calories", "Reset") is logged with event data.
- **Conversion Data**: Tracks non-organic installations and captures relevant attribution data.

To test and track in-app events, follow the [AppsFlyer documentation](https://dev.appsflyer.com/hc/docs).

### Event Tracking Example:
```java
logButtonEvent("Sugar");  // Tracks when the Sugar sorting button is clicked
