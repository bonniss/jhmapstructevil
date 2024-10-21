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
jhipster jdl ../main.jdl --skip-client --skip-install --skip-git

# At folder `with-dto`
jhipster jdl ../main.jdl --inline "dto * with mapstruct" --skip-client --skip-install --skip-git
```

## How to benchmark?

It's no-brainer: Build each folder for X times (typically 5) and display the average build time.

```sh
# At folder `jh-10`
chmod +x benchmark.sh
./benchmark.sh
```

## Results
