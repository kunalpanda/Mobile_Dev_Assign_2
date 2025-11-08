# 📍 SpotFinder

> Your friendly GTA location finder & manager

[![Android](https://img.shields.io/badge/Android-24%2B-brightgreen)]()
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9-purple)]()
[![Room](https://img.shields.io/badge/Room-2.6.1-blue)]()

## ✨ What's This?

SpotFinder is a sleek Android app that helps you explore and manage 100+ locations across the Greater Toronto Area. Search, add, update, or delete locations—all displayed beautifully on an interactive map!

## 🎯 Features

- 🔍 **Search** - Find locations by address and view them instantly on the map
- ➕ **Add** - Add new GTA locations with coordinates
- ✏️ **Update** - Modify existing location details easily
- 🗑️ **Delete** - Remove locations you no longer need
- 📋 **View All** - Browse complete list of stored locations
- 🗺️ **Interactive Map** - Visualize locations with custom markers

## 🛠️ Tech Stack

![Kotlin](https://img.shields.io/badge/-Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/-Android-3DDC84?style=flat&logo=android&logoColor=white)
![Room](https://img.shields.io/badge/-Room%20DB-4285F4?style=flat&logo=sqlite&logoColor=white)
![Google Maps](https://img.shields.io/badge/-Google%20Maps-4285F4?style=flat&logo=google-maps&logoColor=white)

## 🚀 Quick Start

```bash
# Clone the repo
git clone https://github.com/yourusername/Mobile_Dev_Assign_2.git

# Open in Android Studio and run!
```

💡 **Note:** The app includes a Google Maps API key. For production, replace it with your own from [Google Cloud Console](https://console.cloud.google.com/).

## 📊 Database Schema

```kotlin
Location {
    id: Int              // Primary Key (Auto-generated)
    address: String      // Full location address
    latitude: Double     // Latitude coordinate
    longitude: Double    // Longitude coordinate
}
```

## 📍 Coverage Areas

Pre-loaded with 100+ locations across: **Oshawa** • **Ajax** • **Pickering** • **Scarborough** • **Downtown Toronto** • **North York** • **Etobicoke** • **Mississauga** • **Brampton** • **Vaughan**

## 🎓 Project Info

**Course:** SOFE 4640U - Mobile Application Development  
**Term:** Fall 2025  
**Institution:** Ontario Tech University  
**Instructor:** Dr. Nasim Beigi-Mohammadi

---

Built with ❤️ by Kunal | Educational Project
