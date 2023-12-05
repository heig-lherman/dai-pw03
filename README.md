# Practical Work 3 - Distributed System Monitoring

## Concept summary

```mermaid
flowchart LR
    P1C[PC1:CPU] --> C
    P2C[PC2:CPU] --> C
    P2M[PC2:RAM] --> C
    P3D[PC3:DSK] --> C
    C{Aggregator}
    C <--> R1[Reader 1]
    C <--> R2[Reader N]
    style C stroke:orange 
```

Any PC can send data to the aggregator in a fire-and-forget manner.


Readers can then access the aggregated results from the different system producers using
a simple messaging protocol able to filter 
