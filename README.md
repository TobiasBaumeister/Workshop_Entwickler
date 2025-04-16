# Workshop Developer Project

## Overview

This project is a application designed to help developers learn and practice software development concepts.

## Project Structure

```
Workshop_Entwickler/
├── ProductApiApplication/     # Main Spring Boot application
│   ├── src/                   # Source code
│   ├── target/               # Build output
│   └── pom.xml               # Maven configuration
├── aufgaben/                 # Workshop tasks and exercises
└── .gitignore               # Git ignore configuration
```

## Prerequisites

- Java JDK 17 or higher
- Maven 3.8 or higher
- Git
- An IDE (IntelliJ IDEA recommended)

## Getting Started

### Installation

1. Clone the repository:
   ```bash
   git clone [repository-url]
   ```

2. Navigate to the project directory:
   ```bash
   cd Workshop_Entwickler
   ```

3. Build the project:
   ```bash
   cd ProductApiApplication
   mvn clean install
   ```

### Running the Application

1. Start the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

2. The application will be available at: `http://localhost:8080`

## Features

- RESTful API implementation
- Product management system
- Spring Boot best practices
- Workshop exercises and tasks

## Development

- The project uses Spring Boot for the backend
- Maven for dependency management
- Follows REST API design principles
- Includes workshop materials for learning

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contact

For any questions or support, please contact the project maintainers.
