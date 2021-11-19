== Business Components Framework through Java EE: BCF Approach Design Guidelines ==

=== Document audience ===

This guideline is intended for use by various groups specifying network and service management interfaces
within BCF.

=== Introduction ===

The BCF Design Guidelines (BCF DG) defines a set of modeling patterns that can be applied to the Java EE
platform for the specification of EJB-based network and service management building blocks. From the
perspective of the BCF Design Guidelines, the following characteristics become critical:
* BCF functions are specified and implemented as EJB Components, providing
* Coarse Grained, Business Oriented Interfaces, supporting
* Clustering, Scalability and Fail Over via the Application Server, utilizing
* Messaging to minimize Component Coupling, facilitating
* Process Integration and Component Orchestration relying on
* JCA Connectors for the integration of heritage systems

Key to the BCF DG is the concept of a software building block. A software building block is a collection
software components, that when bundled together for the purposes of deployment, meet one or more
business needs. An BCF  Building Block comprises a number of different types of software components.
These include, but are not limited to
* EJB Session Beans with POJO
* EJB Entity Beans
* Message Driven Beans

An BCF Building Block is a piece of component based software responsible for the management of
traditional telecommunication managed entities or less traditional telecommunication business objects. The less traditional telecommunication business objects include, but are not limited to such entities as:
* Rules within a rules engine
* Service Orders
* QoS Objectives
* SLA components

In the BCF DG a managed entity or business object is represented by an **EJB Entity Bean** or a 
**POJO**. The BCF functionality is implemented within software components supporting contract defined interfaces that represent externalized application logic, i.e., BCF interfaces are **EJB Session Beans** providing a coarse-grained workflow oriented view of the managed entities

The BCF Design Guidelines focuses the following elements:
*  A mechanism to implement an asynchronous and a loosely coupled interaction model
*  A mechanism for strongly typed, object based events
*  A mechanism to facilitate the location of BCF architectural elements, (i.e. Session Beans)
*  Reliance on the well understood facade pattern as a template for the BCF Session Bean interfaces, utilizing POJO objects as parameters.


=== Concepts ===

==== Entities ====

An ** entity type **  describes a type of “thing” in the real world with an independent existence. An ** entity type ** may be an object with a physical existence – a circuit pack, managed element, or slot – or it may be an object with a conceptual existence – an alarm, a rule-set, a SLA, a Service Order, a Trouble Ticket, a Scanner, a subnetwork, termination point, etc. Each ** entity type ** has particular properties, termed attributes that describe the entity. An **entity instance** (or **entity**) describes a particular instance of an **entity  type** (e.g., Circuit Pack No. 1). Each **entity**’s attributes are described by particular values that represent the state of that instance. In addition, each **entity **must be uniquely identifiable.

As previously noted an entity can be an** EJB Entity Bean** or an **POJO** object . A BCF building block provides interfaces to manage a specific set of entities. Different interaction models using different representations of the same managed entities are identified.

In the BCF Design Guidelines, entities are passed to and from interfaces based on a number of well
established Java EE design patterns and practices. These are:
*  Session Façade Pattern
*  Value Object (POJO) Pattern
*  Factory Pattern
*  Iterators for Bulk Data Transfer
*  Generic Queries
*  Bulk Operations with Best Effort and Atomic Semantics
*  Meta-Data Capabilities with Factories

==== Interaction Patterns ====

The previous patterns and practices provide structural references for the design of the BCF Design
Guidelines; however, interaction patterns are necessary to understand the operation of the BCF  DG. An
interaction pattern defines the nature of the interactions (i.e., invocation interfaces) exposed by BCF Building Blocks.
The BCF  defines multiple levels at which **entities **may be accessed (i.e., how information is exposed
via an interface). The definition of different interaction patterns depending on the type of actor accessing
the managed system.
The following table summarizes the intended use and the type of interaction patterns that are recommended:

|= **Intended Use** |= ** Interaction Pattern ** |
| Tight integration with BCF Through Java applications | Java Value Type Session Bean (JVT) |
| Loose integration with BCF Through Java applications \\in BCF web browser scenarios | AJAX/JMS Messaging |

The interaction patterns defined  are:
*  **Entity Session Beans** : Instances of a well-defined set of
entity types are accessed via a single interface i.e., a Stateless Session Bean.
Bulk operations are defined in the application-specific Session interfaces, which pass identities and states of managed entities using operation parameters using POJO objects. ** JVT style interfaces are intended for use by Java clients that desire tight integration with BCF Through Java applications**.
*  **Messaging**: Instances of a well-defined set of entity types are accessed via requests sent to Messaging Destinations. The requests are routed to application specific Message Driven Beans. Operations are defined in Request messages, which pass identities and states of managed entities using operation parameters defined by a Schema **employing the mappings of the Java objects**. It is expected that the Stateless Session Beans will be used to implement the interfaces supporting the messages.

==== Entity Session Interface ==== 
The Entity Session Bean provides access to the managed entities using Java™ Value Type (JVT) objects as
parameters and return types. In this interaction pattern, the managed entities are represented as POJO objects based on object model. The Entity Session Bean support interfaces to:
*  Create an Entity object
*  Update an Entity object
*  Query one or more Entity objects

==== Messaging ==== 
TODO

==== Enterprise Application Integration ==== 
TODO

=== Modeling Entity Session Beans ===

The following section provides guidelines for modeling Session Beans with Java Managed Entities Value
Type parameters. This type of Session Bean provides access to the managed entities as Java Value Objects.
Design guidelines are defined for:
* Modeling Entity Key Interfaces
* Modeling Entity Interfaces
* Modeling Constants and Enumerated Types
* Modeling Session Beans Operations with Java Entities Value Type Parameters
* Modeling Best Effort and Atomic Operations
* Modeling Iterators
* Modeling Exceptions in JVT Operations
* Modeling Queries


==== EntityKey Interface ====

**The entity key encapsulates all the information that is necessary to identify a particular entity instance**. Every Entity Type must have a corresponding Entity Key interface defined for it. Note
that two entity types could share the same entity key. The Entity Key is the unique identifier for the Entity. A complete definition of the  EntityKey interface is provided below:

{{{
#!java
package com.inlabsoft.hikari.bcf;

import java.io.Serializable;

import com.inlabsoft.hikari.core.CloneableInstance;

/**
 * The <code>EntityKey</code> interface represents a unique identifier of the {@link Entity}. It
 * specifies multicomponent key for the {@link Entity} and can hold one or more properties that
 * uniquely identifying the {@link Entity}.
 * <p>
 * A count of components of the key affects on key's complexity. If key has more than one component
 * such key is complex ({@link #isComplex()}). A set of components for a key must be specified once
 * and in the key creation time. This means that structure of the key cannot be changed during
 * runtime and must be a part of entity structure. In other words a set of key's components must be
 * specified in the entity's design time.
 * <p>
 * The <code>EntityKey</code> provides ability to work with key's components. But implementation
 * must guarantee that set of key's components cannot be changed during runtime. So such methods
 * {@link #removeComponent(String)} cannot remove specified key's component, it only clears value
 * from component. This means that addressing to unspecified key's component will prevent to
 * {@link IllegalArgumentException}. To avoid this should be used {@link #getComponentNames()}
 * method.
 * <p>
 * The <code>EntityKey</code> gives a two work modes - work with packed value of key and work with
 * key's components. It is necessary to know, that every implementation should synchronize packed
 * value of key with its unpacked part - components. When with packed key happens changes, it must
 * unpacks into components. Also, if changes happens with components - they should be packed into
 * the key. So, packed and unpacked parts of <code>EntityKey</code> are two sides of one thing for
 * every time.
 *
 * @author  Andrey Ochirov
 * @version 1.0
 *
 * @see     Entity
 */
public interface EntityKey<E extends Entity<E>> extends CloneableInstance<EntityKey<E>>,
    Comparable<EntityKey<E>>, Serializable {

    /**
     * The empty array of entity keys.
     */
    @SuppressWarnings("unchecked")
    public EntityKey[] EMPTY_ENTITY_KEY_ARRAY = new EntityKey[0];

    /**
     * Clears key to empty state, when no any key components can't be found.
     * <p>
     * This method not affect to {@link #getComponentNames()} method. It only clear values from
     * this key.
     */
    public void clear();

    /**
     * Makes a deep copy of this key.
     *
     * @return  deep copy of this key.
     */
    public EntityKey<E> clone();

    /**
     * Gets the value of specified key's components.
     *
     * @param   name the name of key's component.
     * @return  the value of key's component.
     *
     * @throws  IllegalArgumentException in case of component name not supported by this key.
     *
     * @see     #setComponent(String, Object)
     */
    public Object getComponent(String name);

    /**
     * Gets the names of key's components.
     *
     * @return the array of key's components.
     */
    public String[] getComponentNames();

    /**
     * Gets the values of specified key's components.
     *
     * @param   names the array of names of key's components.
     * @return  the array of values of key's components.
     *
     * @throws  IllegalArgumentException in case of one or more component names not supported by
     *          this key.
     */
    public Object[] getComponents(String[] names);

    /**
     * Returns packed version this key.
     *
     * @return  packed version of this key.
     *
     * @see     #setKey(String)
     */
    public String getKey();

    /**
     * Tests if the specified name is a name of one of this key's components.
     *
     * @param   name a possible component name.
     * @return  <code>true</code> if and only if the specified string is a name of this key's
     *          component, as determined by the <code>equals</code> method; <code>false</code>
     *          otherwise.
     */
    public boolean hasComponent(String name);

    /**
     * Tests this key on complexity.
     *
     * @return  the flag of key's complexity. If key has more than one component, method returns
     *          <code>true</code>, otherwise it returns <code>false</code>.
     */
    public boolean isComplex();

    /**
     * Tests this key on emptiness.
     *
     * @return  the flag of key's emptiness. If key has no one component, method returns
     *          <code>true</code>, otherwise it returns <code>false</code>.
     */
    public boolean isEmpty();

    /**
     * Removes from this key specified component.
     *
     * @param   name the name of key's component to remove.
     * @return  value of the component, that was removed from this key.
     *
     * @throws  IllegalArgumentException in case of component name that not supported by this key.
     */
    public Object removeComponent(String name);

    /**
     * Removes from this key specified components.
     *
     * @param   names the array of names of key's components to remove.
     * @return  the array of values of the components, that was removed from this key.
     *
     * @throws  IllegalArgumentException in case of one or more component names not supported by
     *          this key.
     */
    public Object[] removeComponents(String[] names);

    /**
     * Sets a component of this key by specified <code>name</code> with specified
     * <code>value</code>.
     * <p>
     * The value of key's component can be retrieved by calling the {@link #getComponent(String)}
     * method.
     *
     * @param   name the name of key's component.
     * @param   value the key's component value.
     *
     * @throws  IllegalArgumentException in case of component name that not supported by this key.
     *
     * @see     #getComponent(String)
     */
    public void setComponent(String name, Object value);

    /**
     * Sets the packed value of key.
     *
     * @param   key the packed value of key.
     *
     * @throws  IllegalArgumentException in case of invalid string representation of the key or if
     *          specified key contains one or more unsupported component names.
     *
     * @see     #setKey(String)
     */
    public void setKey(String key);

    /**
     * Returns a human-readable string representation of this <code>EntityKey</code> object in the
     * form of a set of components, enclosed in braces and separated by the ASCII characters
     * "<code>,&nbsp;</code>" (comma and space). Each component is rendered as the component name,
     * an equals sign <code>=</code>, and the associated value, where the <code>toString</code>
     * method is used to convert the component name and component value to strings.
     * <p>
     * Overrides to {@link Object#toString()} method.
     *
     * @return  a human-readable string representation of this key.
     */
    public String toString();

}
}}}

A specific Entity Key interface must be parameterized for each concrete type of Entity by corresponding Entity type.

The BCF provides the abstract and concrete classes of Entity Key types, which can be used or extended as a primary key definition for an Entity, for example: 

===== AbstractEntityKey =====
{{{
#!java
package com.inlabsoft.hikari.bcf;

/**
 * The <code>AbstractEntityKey</code> class is a base class for single implementations of the
 * {@link EntityKey} interface. It implements common methods and helps to final implementations.
 */
public abstract class AbstractEntityKey<V extends Comparable<V>,
        T extends AbstractEntityKey<V, T, E>, E extends Entity<E>>
    implements EntityKey<E> {

    ...

}
}}}
===== AbstractComplexEntityKey=====
{{{
#!java
package com.inlabsoft.hikari.bcf;

/**
 * The <code>AbstractComplexEntityKey</code> class is a base class for
 * implementations of the {@link EntityKey} interface. It implements common
 * methods and helps to final implementations.
 */
public abstract class AbstractComplexEntityKey<T extends AbstractComplexEntityKey<T, E>,
        E extends Entity<E>> extends AbstractObject<T>
    implements EntityKey<E> {

    ...

}
}}}

===== IntegerEntityKey=====

{{{
#!java
package com.inlabsoft.hikari.bcf.key;

/**
 * The <code>IntegerEntityKey</code> class is a single value entity key that
 * used {@link Integer} value in key.
 *
 * @see     AbstractEntityKey
 */
public class <IntegerStringEntityKey<E extends Entity<E>>
    extends AbstractEntityKey<Integer, IntegerEntityKey<E>, E> {

    ...

}
}}}

===== ObjectEntityKey=====
{{{
#!java

package com.inlabsoft.hikari.bcf.key.complex;

/**
 * The <code>ObjectEntityKey</code> class is a reference complex implementation
 * of the {@link EntityKey} interface that stores as parts of the key any
 * serializable object.
 */
public class ObjectEntityKey<E extends Entity<E>>
    extends AbstractComplexEntityKey<ObjectEntityKey<E>, E> {

    ...

}

}}}


==== Equality of Managed Entity Keys ====

TODO

==== EntityName Interface ====

TODO


==== AttributeAccessible Interface ====

The AttributeAccessible is a base interface from which all Value objects must derive from. It represents both an attribute dirty marker and a generic attribute accessor. All classes implementing a value interface provide several ways to access the attributes:
*  attributes can be accessed through standard Java Beans get/set (is/set)
*  attributes can be accessed through the AttributeAccessible generic methods.
A complete definition of the jAttributeAccessible  interface is provided below:

{{{
#!java
package com.inlabsoft.hikari.bcf;

import java.util.Map;

/**
 * The <code>AttributeAccessible</code> is an interface that declares an
 * attribute access through attribute name. In other words a map-like attribute
 * access to attributes of the implementation.
 * <p>
 * Every class that represents <code>AttributeAccessible</code> interface must
 * provide several ways to access attributes:
 * <ul>
 * <li>Attributes can be accessed through standard JavaBeans get/set (is/set)
 * methods.</li>
 * <li>Attributes can be accessed through the generic methods:
 * <ul>
 * <li>{@link #getAttributeValue(String)}</li>
 * <li>{@link #setAttributeValue(String, Object)}</li>
 * </ul>
 * </li>
 * </ul>
 * A client needs to know which attributes exist in order to provide correct
 * attribute names. It can get this information with a call to {@link
 * #getAttributeNames()}.
 *
 * @version 1.0
 */
public interface AttributeAccessible {

    /**
     * Returns all populated attribute values. If no attributes are populated
     * an empty {@link Map} is returned. Method never returns <code>null</code>.
     *
     * @return  a map of all populated attributes ([name,value] pair).
     */
    public Map<String, Object> getAllPopulatedAttributes();

    /**
     * Returns all attribute names, which are available in this value object.
     * Method never returns <code>null</code>.
     * <p>
     * The returned names may be used as arguments to the generic methods:
     * <ul>
     * <li>{@link #getAttributeValue(String)}</li>
     * <li>{@link #setAttributeValue(String, Object)}</li>
     * </ul>
     * <p>
     * This method may be used by generic clients to obtain information on the
     * attributes. It does not say anything about the state of an attribute,
     * i.e. if it is populated or not.
     *
     * @return  an array containing all attribute names in no particular order.
     */
    public String[] getAttributeNames();

    /**
     * Returns the value of the specified attribute.
     *
     * @param   attributeName the attribute's name.
     * @return  the attribute's value. Primitive types are wrapped in their
     *          respective classes.
     *
     * @throws  AttributeAccessException in case when specified attribute name
     *          points on unavailable or unpopulated attribute.
     *
     * @see     #setAttributeValue(String, Object)
     */
    public <E> E getAttributeValue(String attributeName);

    /**
     * Returns multiple attribute values given an array of attribute names.
     * Method never returns <code>null</code>.
     *
     * @param   attributeNames an array containing attribute names in no
     *          particular order.
     * @return  a map of specified attributes ([name,value] pair). This method
     *          returns map only if specified attributes are available and
     *          populated. If specified <code>null</code> or empty array method
     *          returns empty map.
     *
     * @throws  AttributeAccessException in case when specified attribute names
     *          contains points on one or more unavailable or unpopulated
     *          attributes.
     *
     * @see     #setAttributeValues(Map)
     */
    public Map<String, Object> getAttributeValues(
            String[] attributeNames);

    /**
     * Returns the names of all populated attributes. Method never returns
     * <code>null</code>.
     * <p>
     * Although an attribute is populated, it can be <code>null</code>.
     *
     * @return  names of all populated attributes. When no attributes are
     *          populated an <b>empty array</b> is returned. It is required to
     *          return a subset of the array returned by
     *          {@link #getAttributeNames()}.
     */
    public String[] getPopulatedAttributeNames();

    /**
     * Returns <code>true</code>, if all attributes in this value object are
     * populated.
     *
     * @return  <code>true</code>, if all attributes are populated,
     *          <code>false</code> otherwise.
     *
     * @see     #isPopulated(String attribute)
     */
    public boolean isFullyPopulated();

    /**
     * Checks if a specific attribute is populated.
     *
     * @param   attributeName the name of the attribute which is to be checked
     *          for population.
     * @return  <code>true</code>, if this attribute is populated,
     *          <code>false</code> otherwise.
     *
     * @see     #isFullyPopulated()
     */
    public boolean isPopulated(String attributeName);

    /**
     * Marks all the attributes as populated.
     *
     * @see     #populateAttribute(String)
     * @see     #unpopulateAllAttributes()
     */
    public void populateAllAttributes();

    /**
     * Marks a single attribute as populated. After this call {@link
     * #getAttributeValue(String)} must return attribute value which by default
     * is <code>null</code>.
     *
     * @param   attributeName the name of the attribute to be populated.
     *
     * @see     #populateAllAttributes()
     * @see     #unpopulateAttribute(String)
     */
    public void populateAttribute(String attributeName);

    /**
     * Assign a new value to an attribute.
     *
     * @param   attributeName the attribute's name which shall be changed.
     * @param   value the attribute's new value. This can either be:
     *          <ul>
     *          <li>An object of the type associated with
     *          <code>attributeName</code></li>
     *          <li>A wrapper object for a primitive type, e.g. an
     *          <code>Integer</code> wrapping an <code>int</code></li>
     *          </ul>
     *
     * @see     #getAttributeValue(String)
     */
    public void setAttributeValue(String attributeName, Object value);

    /**
     * Sets multiple attribute values.
     *
     * @param   attributeNamesAndValuePairs is a map which contains an
     *          attribute values with attribute name as a key.
     *
     * @see     #getAttributeValues(String[])
     */
    public void setAttributeValues(
            Map<String, Object> attributeNamesAndValuePairs);

    /**
     * Reset all the attributes to unpopulated.
     *
     * @see     #populateAllAttributes()
     * @see     #unpopulateAttribute(String)
     */
    public void unpopulateAllAttributes();

    /**
     * Marks a single attribute as unpopulated. After this call
     * {@link #getAttributeValue(String)} must raise the {@link
     * AttributeAccessException}.
     * <p>
     * All unpopulated attributes must be nullified.
     *
     * @param   attributeName the name of the attribute to be unpopulated.
     *
     * @see     #populateAttribute(String)
     * @see     #unpopulateAllAttributes()
     */
    public void unpopulateAttribute(String attributeName);

}
}}}


==== Entity Interface ====

For every entity type there must be a Java  entity value interface defined. That is for every type of managed entity there must be a <Managed>Entity java interface defined. __The <Managed>Entity interface must extend the base Entity  interface__.
A managed entity value must have public set and get methods for all the attributes supported by the
managed entity.

An  entity must have public set and get methods for all the attributes supported by the entity.

This guideline defines a base  Entity interface and the value types defined for managed entities must implement this base type. 
A complete definition of the  Entity interface is provided below:
{{{
#!java
package com.inlabsoft.hikari.bcf;

import java.io.Serializable;

import com.inlabsoft.hikari.bcf.dynamic.DynamicExtensionEntity;
import com.inlabsoft.hikari.core.CloneableInstance;

/**
 * The <code>Entity</code> interface is the definition of the Entity, where Entity represents a
 * business object from business requirement for an application.
 *
 * @version 1.0
 */
public interface Entity<E extends Entity<E>> extends AttributeAccessible,
    CloneableInstance<E>, Serializable {

    /**
     * Returns implementation class of this entity.
     * <p>
     * Never returns <code>null</code>.
     */
    public Class<?> getEntityClass();

    /**
     * Returns the key for this object. The key is unique over all objects. Method never returns
     * <code>null</code>. For newly created entities method will return empty key (see for details
     * {@link EntityKey#isEmpty()}).
     *
     * @return  the key for this value object.
     *
     * @see     #setEntityKey(EntityKey)
     * @see     EntityKey
     */
    public EntityKey<E> getEntityKey() throws AttributeAccessException;

    /**
     * Returns the name of this entity which can be used in the {@link EntitySessionManager} to
     * find {@link EntitySession} for this entity.
     *
     * @return an entity name. Never returns <code>null</code>.
     *
     * @see     #setEntityName(EntityName)
     * @see     EntityName
     */
    public EntityName getEntityName();

    /**
     * Returns extension entity of this entity. Returned extension entity is parameterized with
     * this entity and represents optional attributes of this entity.
     *
     * @return  the extension entity.
     *
     * @see     #setExtension(DynamicExtensionEntity)
     */
    public DynamicExtensionEntity<E> getExtension();

    /**
     * Set a new key for this entity.
     * <p>
     * May be used when there is a need to search for an specific entity using this value as a
     * template.
     *
     * @param   key the new value for the key.
     *
     * @throws  AttributeAccessException when the given key is not of correct type. Typically,
     *          entity key instantiated by corresponding {@link EntitySession}.
     *
     * @see     #getEntityKey()
     * @see     EntityKey
     */
    public void setEntityKey(EntityKey<E> key) throws AttributeAccessException;

    /**
     * Sets the name for this entity.
     *
     * @param   name the name for an entity. <code>Null</code> value is not acceptable.
     *
     * @see     #getEntityName()
     * @see     EntityName
     */
    public void setEntityName(EntityName name);

    /**
     * Sets extension entity to this entity. Specified extension entity MUST be parameterized with
     * this entity and represents optional attributes of this entity.
     *
     * @param   extension the extension entity.
     *
     * @see     #getExtension()
     */
    public void setExtension(DynamicExtensionEntity<E> extension);

}

}}}

For every managed entity type there must be a Java managed entity value interface defined. A managed
entity value interface must provide public set and get methods for all the attributes supported by the
managed entity. A managed entity value interface must contain a getEntityKey() and
setEntityKey() methods for getting and setting the managed entity keys.


=== Entity Value Operations ===

**The <Managed>Entity Java interface must include getters and setters for each of the attributes
supported by the managed entity. Set operations must be defined even for immutable attributes so
that the Value object can be used in Template based matching, we also need the capability of setting
the key as part of the managed entity value. Exceptions should be raised in the Entity Session Bean if
an attribute cannot be set.
**

TODO


==== Equality of Managed Entity Values ====
The implementation of the equals method on an Entity Value must be compliant with the following
rule:
Two entity values are equals:
*  if their managed entity keys are equal
*  if they contain the same populated attribute names
*  if each populated attribute value are equal
Note that if the attribute type is an Array then the API must specify if the element order is important in the
equality testing.

==== DymamicEntity and DymamicEntityExtension Interfaces ====

TODO


==== Modeling Entity Value Operations ==== 

The following section provides guidelines for modeling Entity Session Beans operations. Guidelines are
provided for the specification of the operations supporting the following capabilities:
* **Create an Entity Object**  for a specific Entity type.
* **Get a Single Entity** given its key and an attribute selection list.
* **Get Multiple Entities** given their keys and an attribute selection list.
* **Get Multiple Entities** matching a value template and an attribute selection list
* **Get Multiple Entities** matching at least one of multiple value templates and an attribute  selection list
* **Setting a Single Entity** given a Value Object
* **Setting Multiple Entities** each with different values
* **Setting Multiple Entities** given their ID with the same value
* **Setting Multiple Entities** matching a single template with the same value
* **Setting Multiple Entities** matching at least one of multiple value templates with the same value
* **Creation of Multiple Entities** each with a different value (with or without auto naming)
* **Creation of Multiple  Entities** each with the same value (no auto naming)
* **Removing a Entity** matching a given key
* **Removing Multiple  Entities** given their keys.
* **Removing multiple Entities **matching at least one of the template
* **Query Multiple Entities** using a Query Object

//**Note that overloading of the business methods in a Entity Session Bean is prohibited.**//


==== Best Effort and Atomic Operations in Entity Session Beans ====

This specification makes the distinction between two types of Entity Session Bulk operations:
* // Best Effort Bulk Operations
where the SET, REMOVE, CREATE bulk operations may be partially successful i.e. modifying a
subset of the targeted managed entities is a valid operation result .The intent of this type of
operation is to package multiple updates in a single invocation. Note that every single update
targeted by the bulk operation are not related or bound together. This type of operation should be
used as an optimization to minimize remote invocation delays and when it is not necessary to make
sure that all the updates are performed as a single unit or in all or nothing fashion.//
*//  Atomic Bulk Operations
where the SET, REMOVE, CREATE bulk operations must be totally successful i.e. all the
attributes of a managed entity must be set and all the managed entities must be set with the
provided values otherwise all the entities are reverted back to their previous values. The intent of
this type of operation is to package multiple updates in a single invocation. Note that a good use
case scenario to use that type of operation is where every single update targeted by the bulk
operation are related or bound together in some form of transactional unit of work where the
failure of a single update may affect the success of another packaged update. In the atomic mode
the invocation must be executed as a single unit and no update could fail otherwise the whole
operation must be rolled back as a single unit of work. This type of operation should be used as an
optimization to minimize remote invocation delays and when it is necessary to make sure that all
the updates are performed as a single unit of work in all or nothing fashion//.

//**Note that it is possible to mix atomic and best effort operations within the same bean**//.