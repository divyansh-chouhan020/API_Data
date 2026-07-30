This app demonstrates production-grade Android architecture patterns by consuming product endpoints.
🛠 Tech Stack
UI Framework: Jetpack Compose (Material 3)

Architecture: Clean Architecture + MVVM (Model-View-ViewModel)

Dependency Injection: Dagger - Hilt

Networking: Retrofit 2 + Gson Converter

Asynchronous Code: Kotlin Coroutines + StateFlow

Image Loading: Coil 3 (coil-compose + coil-network-okhttp)

Language: Kotlin

✨ Features
Declarative Grid Layout: Responsive 2-column list rendered with LazyVerticalGrid.

State-Driven UI: UI automatically updates based on immutable ProductState emissions (Loading, Success, Failure).

Asynchronous Network Calls: Non-blocking background network fetching using viewModelScope and Coroutines.

Defensive Parsing & Rendering: Built-in safeguards against malformed URLs, nested objects, and missing API fields.

Dependency Injection: Loose coupling between Network, Repository, ViewModel, and UI layers via Hilt modules.

Project Structure :- 

com.dlancers.api_data/
│
├── data
│   ├── model
│   │    └── Product.kt
│   │
│   ├── remote
│   │    └── ProductApi.kt
│   │
│   └── repository
│        └── ProductRepositoryImpl.kt
│
├── domain
│   └── repository
│        └── ProductRepository.kt
│
├── presentation
│   ├── components
│   │    └── ProductCard.kt
│   │
│   ├── screens
│   │    └── ProductScreen.kt
│   │
│   ├── state
│   │    └── ProductState.kt
│   │
│   └── viewmodel
│        └── ProductViewModel.kt
│
├── di
│   └── AppModule.kt
│
├── utils
│   └── Constants.kt
│
└── MainActivity.kt
