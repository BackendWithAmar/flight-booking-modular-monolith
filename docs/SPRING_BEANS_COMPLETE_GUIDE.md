# Spring Beans - Complete Interview Guide

## Table of Contents
1. [What is a Spring Bean?](#1-what-is-a-spring-bean)
2. [Bean Creation Methods](#2-bean-creation-methods)
3. [Bean Scopes](#3-bean-scopes)
4. [Bean Lifecycle](#4-bean-lifecycle)
5. [Dependency Injection](#5-dependency-injection)
6. [@Component vs @Bean](#6-component-vs-bean)
7. [@Autowired, @Qualifier, @Primary](#7-autowired-qualifier-primary)
8. [Circular Dependencies](#8-circular-dependencies)
9. [Conditional Beans](#9-conditional-beans)
10. [Lazy Initialization](#10-lazy-initialization)
11. [Common Traps](#11-common-traps)
12. [MCQs](#12-mcqs)

---

## 1. What is a Spring Bean?

### Concept
A **Spring Bean** is simply an object that is created, managed, and destroyed by the Spring IoC (Inversion of Control) container.

### Without Spring (Manual Object Creation)
```java
public class Main {
    public static void main(String[] args) {
        // You create objects manually
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);
        UserController userController = new UserController(userService);
    }
}
```

### With Spring (Spring Creates Objects)
```java
@RestController
public class UserController {
    private final UserService userService;
    
    // Spring automatically creates UserService and injects it
    public UserController(UserService userService) {
        this.userService = userService;
    }
}
```

### Key Points
- Spring creates the object (you don't use `new`)
- Spring manages dependencies (automatic injection)
- Spring controls lifecycle (when created, when destroyed)

---

## 2. Bean Creation Methods

### Method 1: Stereotype Annotations (Most Common)

```java
// Generic component
@Component
public class EmailUtil {
}

// Service layer
@Service
public class UserService {
}

// Data access layer
@Repository
public class UserRepository {
}

// Web controller
@Controller
public class WebController {
}

// REST controller
@RestController
public class UserController {
}
```

**How it works:**
1. Spring scans your packages (via `@ComponentScan`)
2. Finds classes with these annotations
3. Creates beans automatically

### Method 2: @Bean in @Configuration (For Third-Party or Complex Setup)

```java
@Configuration
public class AppConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

**When to use:**
- Third-party libraries (you can't add @Component to their classes)
- Complex initialization logic
- Conditional bean creation

### Method 3: XML Configuration (Legacy - Avoid)

```xml
<bean id="userService" class="com.example.UserService"/>
```

---

## 3. Bean Scopes

### Singleton (Default)
**One instance per Spring container**

```java
@Service  // Singleton by default
public class UserService {
    private int counter = 0;
    
    public void increment() {
        counter++;
    }
}
```

**Example:**
```java
// Both controllers get THE SAME UserService instance
@RestController
public class Controller1 {
    @Autowired
    private UserService userService;  // Instance A
}

@RestController
public class Controller2 {
    @Autowired
    private UserService userService;  // Same Instance A
}
```

### Prototype
**New instance every time**

```java
@Service
@Scope("prototype")
public class ReportGenerator {
    private String reportData;
}
```

**Example:**
```java
@RestController
public class ReportController {
    @Autowired
    private ReportGenerator generator1;  // Instance A
    
    @Autowired
    private ReportGenerator generator2;  // Instance B (different!)
}
```

### Request Scope (Web Applications)
**One instance per HTTP request**

```java
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestContext {
    private String requestId;
}
```

### Session Scope (Web Applications)
**One instance per HTTP session**

```java
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShoppingCart {
    private List<Item> items = new ArrayList<>();
}
```

### Comparison Table

| Scope | Instances | Use Case |
|-------|-----------|----------|
| Singleton | 1 per container | Services, Repositories (stateless) |
| Prototype | New every time | Stateful objects, Reports |
| Request | 1 per HTTP request | Request-specific data |
| Session | 1 per HTTP session | User session data, Shopping cart |

---

## 4. Bean Lifecycle

### Lifecycle Phases

```
1. Constructor Called
         ↓
2. Dependencies Injected
         ↓
3. @PostConstruct Method
         ↓
4. Bean Ready to Use
         ↓
5. @PreDestroy Method (on shutdown)
         ↓
6. Bean Destroyed
```

### Complete Example

```java
@Service
public class UserService {
    
    private final UserRepository userRepository;
    
    // STEP 1: Constructor
    public UserService(UserRepository userRepository) {
        System.out.println("1. Constructor called");
        this.userRepository = userRepository;
    }
    
    // STEP 2: Dependencies already injected via constructor
    
    // STEP 3: Post-construction initialization
    @PostConstruct
    public void init() {
        System.out.println("3. @PostConstruct - Bean initialized");
        // Load cache, connect to external service, etc.
    }
    
    // STEP 4: Bean is now ready for use
    public void doWork() {
        System.out.println("4. Bean working...");
    }
    
    // STEP 5: Before bean is destroyed
    @PreDestroy
    public void cleanup() {
        System.out.println("5. @PreDestroy - Cleaning up");
        // Close connections, save state, etc.
    }
}
```

### Output When Application Starts and Stops
```
1. Constructor called
3. @PostConstruct - Bean initialized
4. Bean working...
5. @PreDestroy - Cleaning up
```

### Real-World Use Cases

```java
@Service
public class DatabaseConnectionService {
    
    private Connection connection;
    
    @PostConstruct
    public void connect() {
        // Establish database connection after bean creation
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db");
    }
    
    @PreDestroy
    public void disconnect() {
        // Close connection before application shutdown
        if (connection != null) {
            connection.close();
        }
    }
}
```

---

## 5. Dependency Injection

### Three Types of Injection

#### 1. Constructor Injection (RECOMMENDED ⭐)

```java
@RestController
public class UserController {
    
    private final UserService userService;
    private final EmailService emailService;
    
    // Spring injects dependencies via constructor
    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }
}
```

**With Lombok (Cleaner):**
```java
@RestController
@RequiredArgsConstructor  // Generates constructor for final fields
public class UserController {
    private final UserService userService;
    private final EmailService emailService;
}
```

**Advantages:**
- ✅ Immutable (final fields)
- ✅ Mandatory dependencies (can't create without them)
- ✅ Easy to test (pass mocks in constructor)
- ✅ Thread-safe

#### 2. Setter Injection

```java
@RestController
public class UserController {
    
    private UserService userService;
    
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
```

**Advantages:**
- ✅ Optional dependencies
- ✅ Can change dependency after creation

**Disadvantages:**
- ❌ Mutable (not thread-safe)
- ❌ Object can exist without dependencies

#### 3. Field Injection (NOT RECOMMENDED ❌)

```java
@RestController
public class UserController {
    
    @Autowired
    private UserService userService;
}
```

**Disadvantages:**
- ❌ Can't make final
- ❌ Hard to test (need Spring context)
- ❌ Hides dependencies
- ❌ Not recommended by Spring team

### Comparison

| Feature | Constructor | Setter | Field |
|---------|------------|--------|-------|
| Immutability | ✅ | ❌ | ❌ |
| Mandatory deps | ✅ | ❌ | ❌ |
| Testability | ✅ | ⚠️ | ❌ |
| Spring recommendation | ✅ | ⚠️ | ❌ |

---

## 6. @Component vs @Bean

### @Component - On Classes You Own

```java
@Component
public class EmailService {
    public void sendEmail(String to, String message) {
        // Send email logic
    }
}
```

**How it works:**
- Put annotation on your class
- Spring scans and creates bean automatically
- Use for classes you write

### @Bean - On Methods for Complex Setup

```java
@Configuration
public class AppConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return mapper;
    }
}
```

**How it works:**
- Method in @Configuration class
- Method returns the object
- Spring calls method and registers return value as bean

### When to Use What?

| Scenario | Use |
|----------|-----|
| Your own class | @Component, @Service, @Repository, @Controller |
| Third-party library | @Bean |
| Complex initialization | @Bean |
| Conditional creation | @Bean |
| Simple class | @Component |

### Example: Third-Party Library

```java
// You CAN'T do this (RestTemplate is from Spring library)
@Component  // ❌ Can't modify library code
public class RestTemplate {
}

// You MUST do this
@Configuration
public class AppConfig {
    @Bean  // ✅ Correct way
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

---

## 7. @Autowired, @Qualifier, @Primary

### Problem: Multiple Implementations

```java
public interface PaymentService {
    void processPayment(double amount);
}

@Service
public class CreditCardService implements PaymentService {
    public void processPayment(double amount) {
        System.out.println("Credit card payment: " + amount);
    }
}

@Service
public class PayPalService implements PaymentService {
    public void processPayment(double amount) {
        System.out.println("PayPal payment: " + amount);
    }
}
```

**Problem:**
```java
@RestController
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;  // ❌ Which one? Spring is confused!
}
```

### Solution 1: @Qualifier

```java
@RestController
public class PaymentController {
    
    @Autowired
    @Qualifier("creditCardService")  // Specify bean name
    private PaymentService paymentService;
}
```

**Or with constructor:**
```java
@RestController
public class PaymentController {
    
    private final PaymentService paymentService;
    
    public PaymentController(@Qualifier("payPalService") PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

### Solution 2: @Primary

```java
@Service
@Primary  // Default choice
public class CreditCardService implements PaymentService {
}

@Service
public class PayPalService implements PaymentService {
}
```

```java
@RestController
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;  // ✅ Injects CreditCardService (Primary)
}
```

### Solution 3: Custom Qualifier Names

```java
@Service("creditCard")  // Custom name
public class CreditCardService implements PaymentService {
}

@Service("paypal")  // Custom name
public class PayPalService implements PaymentService {
}
```

```java
@Autowired
@Qualifier("paypal")
private PaymentService paymentService;
```

### Complete Example

```java
// Interface
public interface NotificationService {
    void send(String message);
}

// Implementation 1
@Service
@Primary
public class EmailNotificationService implements NotificationService {
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

// Implementation 2
@Service
public class SmsNotificationService implements NotificationService {
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}

// Usage
@RestController
public class NotificationController {
    
    @Autowired
    private NotificationService defaultService;  // EmailNotificationService (Primary)
    
    @Autowired
    @Qualifier("smsNotificationService")
    private NotificationService smsService;  // SmsNotificationService
}
```

---

## 8. Circular Dependencies

### What is Circular Dependency?

```java
@Service
public class ServiceA {
    @Autowired
    private ServiceB serviceB;  // A needs B
}

@Service
public class ServiceB {
    @Autowired
    private ServiceA serviceA;  // B needs A → Circular!
}
```

**Error:**
```
The dependencies of some of the beans in the application context form a cycle:
   serviceA
      ↓
   serviceB
      ↓
   serviceA
```

### Solution 1: @Lazy (Quick Fix)

```java
@Service
public class ServiceA {
    private final ServiceB serviceB;
    
    public ServiceA(@Lazy ServiceB serviceB) {  // Lazy initialization
        this.serviceB = serviceB;
    }
}

@Service
public class ServiceB {
    private final ServiceA serviceA;
    
    public ServiceB(ServiceA serviceA) {
        this.serviceA = serviceA;
    }
}
```

**How it works:**
- @Lazy creates a proxy
- ServiceB is not created immediately
- Created only when first used

### Solution 2: Setter Injection

```java
@Service
public class ServiceA {
    private ServiceB serviceB;
    
    @Autowired
    public void setServiceB(ServiceB serviceB) {
        this.serviceB = serviceB;
    }
}

@Service
public class ServiceB {
    private final ServiceA serviceA;
    
    public ServiceB(ServiceA serviceA) {
        this.serviceA = serviceA;
    }
}
```

### Solution 3: Redesign (BEST)

```java
// Bad design - circular dependency
@Service
public class OrderService {
    @Autowired
    private PaymentService paymentService;
}

@Service
public class PaymentService {
    @Autowired
    private OrderService orderService;  // Why does payment need order?
}

// Good design - introduce event or mediator
@Service
public class OrderService {
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public void createOrder() {
        // Create order
        eventPublisher.publishEvent(new OrderCreatedEvent());
    }
}

@Service
public class PaymentService {
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        // Process payment
    }
}
```

---

## 9. Conditional Beans

### @ConditionalOnProperty

```java
@Configuration
public class AppConfig {
    
    @Bean
    @ConditionalOnProperty(name = "feature.email.enabled", havingValue = "true")
    public EmailService emailService() {
        return new EmailService();
    }
}
```

**application.properties:**
```properties
feature.email.enabled=true
```

### @Profile

```java
@Configuration
public class DatabaseConfig {
    
    @Bean
    @Profile("dev")
    public DataSource devDataSource() {
        return new H2DataSource();  // In-memory database for development
    }
    
    @Bean
    @Profile("prod")
    public DataSource prodDataSource() {
        return new MySQLDataSource();  // Production database
    }
}
```

**Activate profile:**
```properties
spring.profiles.active=dev
```

### @ConditionalOnClass

```java
@Configuration
public class AppConfig {
    
    @Bean
    @ConditionalOnClass(name = "com.amazonaws.services.s3.AmazonS3")
    public S3Service s3Service() {
        return new S3Service();  // Only if AWS SDK is in classpath
    }
}
```

### @ConditionalOnMissingBean

```java
@Configuration
public class AppConfig {
    
    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Only if no other PasswordEncoder exists
    }
}
```

### Real-World Example

```java
@Configuration
public class CacheConfig {
    
    @Bean
    @Profile("prod")
    @ConditionalOnProperty(name = "cache.enabled", havingValue = "true")
    public CacheManager redisCacheManager() {
        return new RedisCacheManager();  // Redis in production
    }
    
    @Bean
    @Profile("dev")
    public CacheManager simpleCacheManager() {
        return new SimpleCacheManager();  // Simple cache in development
    }
}
```

---

## 10. Lazy Initialization

### Eager (Default)
**Bean created at application startup**

```java
@Service
public class UserService {
    public UserService() {
        System.out.println("UserService created at startup");
    }
}
```

**Output when app starts:**
```
UserService created at startup
```

### Lazy
**Bean created only when first used**

```java
@Service
@Lazy
public class ReportService {
    public ReportService() {
        System.out.println("ReportService created when first used");
    }
}
```

```java
@RestController
public class ReportController {
    
    @Autowired
    @Lazy
    private ReportService reportService;
    
    @GetMapping("/report")
    public String generateReport() {
        reportService.generate();  // ReportService created NOW
        return "Report generated";
    }
}
```

**Output:**
```
// App starts - nothing printed
// User calls /report endpoint
ReportService created when first used
```

### When to Use Lazy?

| Use Lazy | Don't Use Lazy |
|----------|----------------|
| Heavy initialization | Core services |
| Rarely used services | Frequently used services |
| Optional features | Critical components |
| Conditional features | Startup validation needed |

### Global Lazy Initialization

```properties
spring.main.lazy-initialization=true
```

**Warning:** Startup errors won't be detected until runtime!

---

## 11. Common Traps

### Trap 1: Field Injection with Final

```java
@Service
public class UserService {
    
    @Autowired
    private final UserRepository repository;  // ❌ Compilation error!
    // final requires initialization in constructor
}
```

**Fix:**
```java
@Service
public class UserService {
    
    private final UserRepository repository;
    
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}
```

### Trap 2: Forgetting @Configuration

```java
public class AppConfig {  // ❌ Missing @Configuration
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

**Fix:**
```java
@Configuration  // ✅ Required
public class AppConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### Trap 3: Singleton Bean with Prototype Dependency

```java
@Service  // Singleton
public class OrderService {
    
    @Autowired
    private ReportGenerator reportGenerator;  // Prototype
    
    public void process() {
        reportGenerator.generate();  // Always same instance! (Bug)
    }
}

@Service
@Scope("prototype")
public class ReportGenerator {
}
```

**Problem:** Singleton gets prototype injected once, then reuses it.

**Fix: Use Provider**
```java
@Service
public class OrderService {
    
    @Autowired
    private Provider<ReportGenerator> reportGeneratorProvider;
    
    public void process() {
        ReportGenerator generator = reportGeneratorProvider.get();  // New instance
        generator.generate();
    }
}
```

### Trap 4: Multiple @Primary

```java
@Service
@Primary
public class ServiceA implements MyService {
}

@Service
@Primary  // ❌ Can't have two @Primary
public class ServiceB implements MyService {
}
```

**Error:** Multiple beans marked as @Primary

### Trap 5: Bean Name Conflicts

```java
@Service("userService")
public class UserServiceImpl {
}

@Service("userService")  // ❌ Same name
public class UserServiceV2 {
}
```

**Error:** Bean name conflict

### Trap 6: Autowiring in Constructor Before Super()

```java
@Service
public class UserService extends BaseService {
    
    private final UserRepository repository;
    
    public UserService(UserRepository repository) {
        this.repository = repository;
        super();  // ❌ super() must be first
    }
}
```

**Fix:**
```java
public UserService(UserRepository repository) {
    super();  // ✅ First line
    this.repository = repository;
}
```

---

## 12. MCQs

### Question 1
What is the default scope of a Spring Bean?

A) Prototype  
B) Singleton  
C) Request  
D) Session  

**Answer: B) Singleton**

---

### Question 2
Which annotation is used for constructor injection?

A) @Inject  
B) @Autowired (optional in Spring 4.3+)  
C) @Resource  
D) All of the above  

**Answer: B) @Autowired (but optional for single constructor)**

---

### Question 3
What happens if you have two beans of the same type without @Primary or @Qualifier?

A) Spring picks the first one  
B) Spring throws NoUniqueBeanDefinitionException  
C) Spring creates both  
D) Spring ignores both  

**Answer: B) Spring throws NoUniqueBeanDefinitionException**

---

### Question 4
When is @PostConstruct called?

A) Before constructor  
B) After constructor, before dependencies injected  
C) After constructor and dependencies injected  
D) Before bean destruction  

**Answer: C) After constructor and dependencies injected**

---

### Question 5
Which is the recommended dependency injection method?

A) Field injection  
B) Setter injection  
C) Constructor injection  
D) Method injection  

**Answer: C) Constructor injection**

---

### Question 6
What does @Lazy do?

A) Makes bean creation slower  
B) Delays bean creation until first use  
C) Destroys bean after use  
D) Creates multiple instances  

**Answer: B) Delays bean creation until first use**

---

### Question 7
How many instances of a prototype bean are created?

A) One per application  
B) One per request  
C) New instance every time it's requested  
D) One per session  

**Answer: C) New instance every time it's requested**

---

### Question 8
What is the purpose of @Configuration?

A) Configure database  
B) Mark class as containing @Bean methods  
C) Configure logging  
D) Configure security  

**Answer: B) Mark class as containing @Bean methods**

---

### Question 9
Which annotation is used to resolve circular dependencies?

A) @Circular  
B) @Lazy  
C) @Async  
D) @Transactional  

**Answer: B) @Lazy**

---

### Question 10
What is the difference between @Component and @Bean?

A) No difference  
B) @Component on class, @Bean on method  
C) @Component is deprecated  
D) @Bean is faster  

**Answer: B) @Component on class, @Bean on method**

---

### Question 11
When should you use @Qualifier?

A) To qualify for production  
B) When multiple beans of same type exist  
C) To improve performance  
D) To make bean lazy  

**Answer: B) When multiple beans of same type exist**

---

### Question 12
What is the order of bean lifecycle?

A) Constructor → @PostConstruct → Dependencies → @PreDestroy  
B) Dependencies → Constructor → @PostConstruct → @PreDestroy  
C) Constructor → Dependencies → @PostConstruct → @PreDestroy  
D) @PostConstruct → Constructor → Dependencies → @PreDestroy  

**Answer: C) Constructor → Dependencies → @PostConstruct → @PreDestroy**

---

### Question 13
Which scope creates one bean per HTTP request?

A) Singleton  
B) Prototype  
C) Request  
D) Session  

**Answer: C) Request**

---

### Question 14
What does @Primary do?

A) Makes bean creation priority  
B) Sets default bean when multiple candidates exist  
C) Makes bean immutable  
D) Validates bean  

**Answer: B) Sets default bean when multiple candidates exist**

---

### Question 15
Can you use @Autowired on a private field?

A) No, must be public  
B) Yes, Spring uses reflection  
C) Only with @Qualifier  
D) Only in @Configuration  

**Answer: B) Yes, Spring uses reflection (but not recommended)**

---

### Question 16
What happens if @PostConstruct method throws an exception?

A) Exception is ignored  
B) Bean creation fails  
C) Application continues  
D) Only that method fails  

**Answer: B) Bean creation fails**

---

### Question 17
Which is NOT a valid bean scope?

A) Singleton  
B) Prototype  
C) Global  
D) Request  

**Answer: C) Global**

---

### Question 18
What is the purpose of @PreDestroy?

A) Destroy bean immediately  
B) Cleanup before bean destruction  
C) Prevent bean creation  
D) Validate bean  

**Answer: B) Cleanup before bean destruction**

---

### Question 19
Can a singleton bean have a prototype dependency?

A) No, not allowed  
B) Yes, but prototype acts as singleton  
C) Yes, works perfectly  
D) Only with @Lazy  

**Answer: B) Yes, but prototype acts as singleton (trap!)**

---

### Question 20
What does @ConditionalOnProperty do?

A) Sets property value  
B) Creates bean only if property matches condition  
C) Validates property  
D) Encrypts property  

**Answer: B) Creates bean only if property matches condition**

---

## Summary Cheat Sheet

### Bean Creation
```java
@Component / @Service / @Repository / @Controller / @RestController
@Bean in @Configuration
```

### Dependency Injection (Best to Worst)
```java
1. Constructor (Recommended)
2. Setter (Optional dependencies)
3. Field (Avoid)
```

### Scopes
```java
@Scope("singleton")   // Default - one instance
@Scope("prototype")   // New instance each time
@Scope("request")     // One per HTTP request
@Scope("session")     // One per HTTP session
```

### Lifecycle
```java
Constructor → Dependencies → @PostConstruct → Use → @PreDestroy
```

### Multiple Beans
```java
@Primary              // Default choice
@Qualifier("name")    // Specific choice
```

### Conditional
```java
@ConditionalOnProperty
@Profile("dev")
@ConditionalOnClass
@Lazy
```

### Common Patterns
```java
@RequiredArgsConstructor  // Lombok for constructor injection
@Autowired               // Optional for single constructor
final fields             // Immutability
```

---

## Your Project Example

```java
@RestController  // Bean created by Spring
@RequestMapping("/api/users")
@RequiredArgsConstructor  // Constructor injection
public class UserController {
    
    private final UserService userService;  // Singleton, injected by Spring
    
    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        return userService.registeUser(request);
    }
}
```

**What Spring does:**
1. Scans and finds @RestController
2. Creates UserController bean (singleton)
3. Sees it needs UserService
4. Creates/finds UserService bean
5. Injects UserService via constructor (generated by @RequiredArgsConstructor)
6. UserController is ready to handle requests

---

## Practice Questions

1. Why is constructor injection better than field injection?
2. When would you use prototype scope instead of singleton?
3. How do you handle circular dependencies?
4. What's the difference between @Component and @Bean?
5. When should you use @Lazy?
6. How does @Primary differ from @Qualifier?
7. What happens if you forget @Configuration on a config class?
8. Why can't you use final with field injection?
9. What's the purpose of @PostConstruct?
10. How do you create conditional beans?

---

**End of Guide**
