# SuperLi Management System

A comprehensive Java-based management system for SuperLi supermarket chain, featuring employee management, delivery operations, and inventory tracking.

## 🚀 Overview

SuperLi Management System is a multi-module Java application designed to streamline supermarket operations. The system handles employee scheduling, delivery management, truck and driver coordination, and order processing through an intuitive console-based interface.

## ✨ Features

### 🧑‍💼 Employee Management Module
- **Employee Registration & Management**: Add, update, and manage employee information
- **Shift Scheduling**: Create and manage work shifts with employee preferences
- **Ability Management**: Track and assign employee abilities and certifications
- **User Authentication**: Secure login system for employees
- **Branch Management**: Multi-branch support with branch-specific operations

### 🚚 Delivery Management Module
- **Order Management**: Create, view, and track customer orders
- **Delivery Planning**: Automated delivery route planning and optimization
- **Driver Management**: Manage driver information, licenses, and assignments
- **Truck Management**: Track truck inventory, capacity, and maintenance
- **Shipping Zone Management**: Organize deliveries by geographical zones
- **Cargo Tracking**: Detailed cargo management with weight and volume tracking

## 📋 Prerequisites

- **Java 17** or higher
- **Maven 3.6+** for dependency management
- **SQLite** (included as dependency)

## 🛠️ Installation & Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/RoyiAizenstain/SuperLi.git
   cd SuperLi
   ```

2. **Build the project:**
   ```bash
   mvn clean compile
   ```

3. **Run the application:**
   ```bash
   # Option 1: Direct compilation and run
   cd dev/src
   javac -cp "$(find ~/.m2/repository -name "*.jar" 2>/dev/null | tr '\n' ':')." $(find . -name "*.java")
   java -cp ".:$(find ~/.m2/repository -name "*.jar" 2>/dev/null | tr '\n' ':')" Main
   
   # Option 2: Using Maven (if properly configured)
   mvn compile exec:java -Dexec.mainClass="Main"
   ```

## 🎯 Usage

### First Time Setup
When you first run the application, you'll be prompted:
- **Load existing data**: Choose "yes" to load pre-populated database
- **Start fresh**: Choose "no" to create a new temporary database

### Main Menu Navigation
The system provides a main menu with the following options:
1. **Delivery Management** - Plan and track deliveries
2. **Order Management** - Create and manage customer orders
3. **Truck Management** - Manage truck fleet
4. **Driver Management** - Manage driver information and licenses
5. **Shipping Zone Management** - Configure delivery zones

### User Authentication
- Log in with your employee credentials
- Different access levels based on employee roles
- Secure session management

## 📁 Project Structure

```
SuperLi/
├── dev/src/
│   ├── Main.java                    # Application entry point
│   ├── Deliveries/                  # Delivery management module
│   │   ├── DAO/                     # Data access objects
│   │   ├── DTO/                     # Data transfer objects
│   │   ├── Domain/                  # Business logic and entities
│   │   ├── Presentation/            # User interface layer
│   │   ├── Service/                 # Service layer
│   │   └── tests/                   # Unit tests
│   ├── employee/                    # Employee management module
│   │   ├── DataAccess/              # Database access layer
│   │   ├── Domain/                  # Business logic
│   │   ├── Dto/                     # Data transfer objects
│   │   ├── Enums/                   # System enumerations
│   │   ├── Exceptions/              # Custom exceptions
│   │   ├── Presentation/            # UI components
│   │   └── Service/                 # Business services
│   └── Resources/                   # Database and configuration files
├── docs/                            # Documentation and specifications
├── pom.xml                          # Maven configuration
└── README.md                        # This file
```

## 🗄️ Database

The system uses **SQLite** as its database engine:
- **Production Database**: `dev/src/Resources/my_database.db`
- **Temporary Database**: Created automatically for testing
- **Schema**: Automatically initialized on first run
- **Data Persistence**: All changes are automatically saved

### Key Database Tables
- **Employees**: Store employee information and abilities
- **Users**: Authentication and user management
- **Shifts**: Work scheduling and preferences
- **Orders**: Customer orders and cargo details
- **Deliveries**: Delivery planning and tracking
- **Trucks & Drivers**: Fleet and personnel management

## 🔧 Configuration

### Maven Dependencies
- **SQLite JDBC Driver**: Database connectivity
- **JUnit 5**: Unit testing framework
- **TestNG**: Additional testing support

### System Requirements
- **Memory**: Minimum 512MB RAM
- **Storage**: 100MB available space
- **Java Version**: Java 17+ (configured in pom.xml)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add amazing feature'`
4. Push to the branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

### Code Style Guidelines
- Follow Java naming conventions
- Write comprehensive JavaDoc comments
- Include unit tests for new features
- Maintain separation of concerns between layers

## 📄 License

This project is part of an academic assignment for software engineering coursework.

## 🛠️ Troubleshooting

### Common Issues

**"Could not find or load main class Main"**
- Ensure you're in the `dev/src` directory when running
- Check that all Java files are compiled successfully
- Verify classpath includes SQLite JAR

**Database Connection Issues**
- Make sure SQLite driver is in classpath
- Check that `dev/src/Resources/` directory exists
- For first run, choose "no" to create a fresh database

**Maven Build Issues**
- Ensure Java 17+ is installed and configured
- Run `mvn clean` before `mvn compile`
- Check internet connection for dependency downloads

## 🆘 Support

For support and questions:
- Check the documentation in the `docs/` folder
- Review the system specification document
- Examine the employee and delivery instruction manuals

---

**SuperLi Management System** - Streamlining supermarket operations through efficient management tools.
