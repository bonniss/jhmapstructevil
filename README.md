# Examine JHipster with Mapstruct

> [!IMPORTANT]
> JHispter v8.7.1

## Can Mapstruct be evil?

MapStruct is a powerful tool that delegates the heavy lifting of mapping logic to the build phase, resulting in cleaner and simpler mapper code. However, this comes at a cost: increased build time. This repository serves as an experiment to evaluate the impact of MapStruct on JHipster projects, focusing on how the number of entities influences the development experience and build time.

The repository contains multiple folders named `jh<number_of_entities>`, each following the same structure. For example, the folder with 10 entities looks like:

```text
jh10/
├── no-dto/
│   └── .jhipster
├── with-dto/
│   └── .jhipster
├── jh10.jdl
└── benchmark.sh
```

## How code are generated?

Folder names are self-explanatory.

```sh
# At folder `no-dto`
cd no-dto && \
  jhipster jdl ../main.jdl --skip-client --skip-install --skip-git

# At folder `with-dto`
cd with-dto && \
  jhipster jdl ../main.jdl --inline "dto * with mapstruct" --skip-client --skip-install --skip-git
```

## How to benchmark?

It's no-brainer: Build each folder (`mvnw clean install -Dmaven.test.skip=true`) for X times (1-5) and display the average build time.

```sh
# At folder `jh-10`
chmod +x benchmark.sh && ./benchmark.sh
```

## Results

### 10 entities

Simple relationship grid: mainly `ManyToOne`, only 1 `ManyToMany`.

```text
==================
jh10
==================

~~~~~~~~~~~~~~~~~~
Measuring build time for no-dto...
Run 1: 7.325 seconds
Run 2: 8.091 seconds
Run 3: 8.418 seconds
7.944 seconds
~~~~~~~~~~~~~~~~~~

~~~~~~~~~~~~~~~~~~
Measuring build time for with-dto...
Run 1: 9.124 seconds
Run 2: 9.248 seconds
Run 3: 8.964 seconds
9.112 seconds
~~~~~~~~~~~~~~~~~~
```

### 30 entities

Triple the `10 entities` section.

```text
==================
jh30
==================

~~~~~~~~~~~~~~~~~~
Measuring build time for no-dto...
Run 1: 8.561 seconds
Run 2: 8.719 seconds
Run 3: 9.079 seconds
8.786 seconds
~~~~~~~~~~~~~~~~~~

~~~~~~~~~~~~~~~~~~
Measuring build time for with-dto...
Run 1: 11.292 seconds
Run 2: 12.067 seconds
Run 3: 12.055 seconds
11.804 seconds
~~~~~~~~~~~~~~~~~~
```

### 50 entities

x5 the `10 entities` section.

```txt
==================
jh50
==================

~~~~~~~~~~~~~~~~~~
Measuring build time for no-dto...
Run 1: 11.083 seconds
Run 2: 15.753 seconds
Run 3: 9.414 seconds
12.083 seconds
~~~~~~~~~~~~~~~~~~

~~~~~~~~~~~~~~~~~~
Measuring build time for with-dto...
Run 1: 16.191 seconds
Run 2: 13.242 seconds
Run 3: 15.310 seconds
14.914 seconds
~~~~~~~~~~~~~~~~~~
```

### SaaS 30 entities

Describe a SaaS (Software-as-a-service) business model: much the same as `jh30`, plus one "tenant entity" that link to every rest entity.

### SaaS 50 entities

`jh50` with SaaS.

### Realworld app

A real-world app design with a complex network of relationships, including multiple ManyToMany relationships, while also applying to the SaaS model.

```txt
==================
realword
==================

~~~~~~~~~~~~~~~~~~
Measuring build time for no-dto...
Run 1: 8.475 seconds
8.475 seconds
~~~~~~~~~~~~~~~~~~

~~~~~~~~~~~~~~~~~~
Measuring build time for with-dto...
Run 1: 326.540 seconds
326.540 seconds
~~~~~~~~~~~~~~~~~~
```

### Realworld app without `ManyToMany` relationships

The same real world app, though stripped off `ManyToMany` relationships.

```txt
==================
realword-no-manytomany
==================

~~~~~~~~~~~~~~~~~~
Measuring build time for no-dto...
Run 1: 6.412 seconds
6.412 seconds
~~~~~~~~~~~~~~~~~~

~~~~~~~~~~~~~~~~~~
Measuring build time for with-dto...
Run 1: 13.497 seconds
13.497 seconds
~~~~~~~~~~~~~~~~~~
```

### Realworld app 2x

Double entities.

```txt
==================
realword-2x
==================

~~~~~~~~~~~~~~~~~~
Measuring build time for no-dto...
Run 1: 8.935 seconds
8.935 seconds
~~~~~~~~~~~~~~~~~~

~~~~~~~~~~~~~~~~~~
Measuring build time for with-dto...
Run 1: 8026.400 seconds
8026.400 seconds
~~~~~~~~~~~~~~~~~~
```

2-hour build time 💀.

### Realworld app 2x without `ManyToMany` relationships
