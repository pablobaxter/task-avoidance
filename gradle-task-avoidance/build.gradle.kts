tasks.withType<Delete> {
    delete(layout.buildDirectory)
}

tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.ALL
}
