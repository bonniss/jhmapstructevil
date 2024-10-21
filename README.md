# Examine JHipster with Mapstruct

> [!IMPORTANT]  
> JHispter v8.7.1

## Can Mapstruct be evil?

Mapstruct is preferable as it move dirty mapping work to build phase therefore mapper code are simple and clean. But surely it comes with build time cost. This repo is an experiment of using Mapstruct in Jhipster with different entities size to see how far Mapstruct will become an obstruction to development experience.

The repo is a collection of multiple `jh<number_of_entities>` folder with same structure inside. For example, folder with 10 entities:

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
