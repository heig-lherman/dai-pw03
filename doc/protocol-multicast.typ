#import "template.typ": *
#show: ieee.with(
  title: "Practical Work 3\nDAI - HEIG-VD",
  authors: (
    (
      name: "Loïc Herman"
    ),
    (
      name: "Massimo Stefani"
    ),
  )
)

#set heading(numbering: "1.1.1")

= Multicast Distributed System Monitoring Protocol

== Overview

The Multicast Distributed System Monitoring Protocol (MDSMP) serves as a crucial tool for efficiently transmitting
and collecting system metrics in distributed environments. Designed to streamline the process of monitoring,
MDSMP enables systems to send metrics data to a central monitoring server. This protocol is especially well-suited for
scenarios where real-time insights into system performance are essential.

== Protocol

MDSMP employs text-based messages following the Prometheus metrics format, as described in the Prometheus documentation
(https://prometheus.io/docs/concepts/data_model/). This standardization ensures compatibility with Prometheus-based
monitoring and facilitates easy integration into existing monitoring ecosystems.

Since MDSMP is based on UDP, it operates without establishing a formal connection between clients and the server.
This results in a lightweight and efficient communication model, reducing overhead and latency. The port used is 9378.

Multicast addresses groups:
cpu : 230.0.0.1
ram : 230.0.0.2
dsk : 230.0.0.3

All messages exchanged within the MDSMP protocol must be encoded in UTF-8. This standardized encoding scheme ensures
compatibility across diverse systems and prevents data corruption during transmission.

== Messages

=== Sending data

+ ```<type>{value=<value>, host=<host>}```
  + ```<type>``` : Type of the data sent (cpu, ram, dsk)
  + ```<value>``` : Value of the sent data
      + cpu : CPU consumption in percentage
      + ram : RAM consumption in MB
      + dsk : Disk consumption in MB
  + ```<host>``` : Name of the machine sending the data (hostname). Must be unique for each machine.

== Examples

#image("protocol-multicast.svg")
