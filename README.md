# Hibernate ORM

Object Relation Mapping

## Step1: Add the following dependencies
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.2.224</version>
    <scope>runtime</scope>
</dependency>

<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>6.4.4.Final</version>
</dependency>

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.30</version>
    <scope>provided</scope>
</dependency>
```
## Step2: Add the following plugin
```xml
<build>
        <sourceDirectory>src/main/java</sourceDirectory>
        <plugins>
            <plugin>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.5.1</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                </configuration>
            </plugin>
        </plugins>
 </build>
```
## Step3: Create an Entity called Album

* @Entity- This annotation specifies that the class is an entity.
* @Table- This annotation specifies the table in the database with which this entity is mapped.
* @Id- This annotation specifies the primary key of the entity.
* @GeneratedValue- This annotation specifies the generation strategies for the values of primary keys.
* @Column- The @Column annotation is used to specify the mapping between a basic entity attribute and the database table column.

## Step4: Create a Hibernate configuration file - hibernate.cfg.xml under `/src/main/resources`

The configuration file holds details regarding the database and mapping file. Typically, it is recommended to name it hibernate.cfg.xml
```xml
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <!-- JDBC Database connection settings -->
        <property name="connection.driver_class">org.h2.Driver</property>
        <property name="connection.url">jdbc:h2:mem:test</property>
        <property name="connection.username">sa</property>
        <property name="connection.password"></property>
        <!-- JDBC connection pool settings ... using built-in test pool -->
        <property name="connection.pool_size">1</property>
        <!-- Select our SQL dialect -->
        <property name="dialect">org.hibernate.dialect.H2Dialect</property>
        <!-- Echo the SQL to stdout -->
        <property name="show_sql">true</property>
        <!-- Set the current session context -->
        <property name="current_session_context_class">thread</property>
        <!-- Drop and re-create the database schema on startup -->
        <property name="hbm2ddl.auto">create-drop</property>
        <!-- dbcp connection pool configuration -->
        <property name="hibernate.dbcp.initialSize">5</property>
        <property name="hibernate.dbcp.maxTotal">20</property>
        <property name="hibernate.dbcp.maxIdle">10</property>
        <property name="hibernate.dbcp.minIdle">5</property>
        <property name="hibernate.dbcp.maxWaitMillis">-1</property>
        <mapping class=“com.learnspring.Album” />
    </session-factory>
</hibernate-configuration>
```
## Step 5: Create helper class called HibernateHelper

* A helper class can be created to initialise the hibernate SessionFactory.
* In the majority of Hibernate applications, the SessionFactory needs to be instantiated only once during the initialisation of the application.
* This single instance should then be utilised by all the code within a specific process, and any Session should be created using this particular SessionFactory.

* The SessionFactory is designed to be thread-safe and can be shared among multiple threads, while a Session is intended to be used by a single thread at a time.
* To ensure the proper management of the SessionFactory, we can create a HibernateHelper class that configures a singleton SessionFactory instance.
* This instance can then be utilised throughout the application for efficient and reliable Hibernate operations.

The bootstrapping API offers great flexibility, although in the majority of scenarios, it is best understood as a three-step procedure.
```java

    public class HibernateHelper {
    private static StandardServiceRegistry serviceRegistry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Create Registry
                serviceRegistry = new StandardServiceRegistryBuilder().configure().build();

                // Create MetadataSources
                var sources = new MetadataSources(serviceRegistry);

                // Create Metadata
                var metadata = sources.getMetadataBuilder().build();

                // Create Session Factory
                sessionFactory = metadata.getSessionFactoryBuilder().build();

            } catch (Exception e) {
                e.printStackTrace();
                if (serviceRegistry != null) {
                    StandardServiceRegistryBuilder.destroy(serviceRegistry);
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (serviceRegistry != null) {
            StandardServiceRegistryBuilder.destroy(serviceRegistry);
        }
    }
}
```

# Step6: Time to play around save and get operation in the `Main.java`

```java
var albumOne = new Album("Avatar-1", "2010");
var albumTwo = new Album("Avatar-2", "2023");

Transaction transaction = null;

try (var session = HibernateHelper.getSessionFactory().openSession()) {

    // start transaction
    transaction = session.beginTransaction();

    // save album details
    session.save(albumOne);
    session.save(albumTwo);
    // commit transaction
    transaction.commit();

} catch (Exception e) {
    if (transaction != null) {
    transaction.rollback();
    }
    e.printStackTrace();
}

try (var session = HibernateHelper.getSessionFactory().openSession()) {
    List<Album> albums = session.createQuery("from Album", Album.class).list();
    albums.forEach(s -> System.out.println(s.getName()));
} catch (Exception e) {
    if (transaction != null) {
    transaction.rollback();
    }
    e.printStackTrace();
}
```

