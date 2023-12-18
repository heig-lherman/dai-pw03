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

= Distributed System Monitoring Protocol - Multicast

== Overview

The Distributed System Monitoring Protocol (DSMP) is a protocol that allows a system to send metrics to a monitoring server.
The protocol is based on a multicast UDP connection, meaning that the server will listen to a specific multicast address
and port, and the clients will send their metrics to this address and port.

== Protocol

The DSMP protocol uses text-based messages over UDP. The messages are send conforming the prometheus metrics format.
The format is described in the following link: https://prometheus.io/docs/concepts/data_model/

Seen that the protocol is based on UDP, a connection is not established between the client and the server, meaning that the server
will not send any response to the client. The server will only listen to the multicast address and port. The used port is 6343.

Multicast addresses groups:
cpu : 230.0.0.1
ram : 230.0.0.2
dsk : 230.0.0.3

All messages are case sensitive and must be sent using UTF-8 encoding.

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


