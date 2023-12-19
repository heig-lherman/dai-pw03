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

// set section number to 2
#counter(heading).step()
= Unicast Distributed System Monitoring Protocol

== Overview

UDSMP serves a critical role in the realm of distributed systems, offering a robust solution for tracking resource
utilization across various components within a distributed network. Its primary objective is to transmit essential
data related to CPU, RAM, and disk consumption from individual machines to system administrators and operators to make
informed decisions regarding system performance and resource allocation.

== Protocol

The protocol follows a request-response pattern, where readers send requests to the monitoring server and receive
responses in return using the port 6343. This pattern facilitates two-way communication, enabling clients to query the
server for specific information.

UDSMP enforces strict case sensitivity for all its messages. This consistency ensures that data is accurately
interpreted and processed by both the sending and receiving ends, eliminating potential issues caused by case mismatches.

All messages exchanged within the UDSMP protocol must be encoded in UTF-8. This standardized encoding scheme ensures
compatibility across diverse systems and prevents data corruption during transmission.

Clients must send the first request to which the server might answer, it is recommended to have a timeout of 1 second in all implementations in case the message gets lost or the server is unavailable.

== Messages

=== From reader to aggregator
+ `GET_EMITTERS` : Request to get the list of emitters
+ `GET_EMITTER <emitter>` : Request to get the values of a specific emitter
  + `<emitter>` : Hostname of the emitter to get the values from

=== From aggregator to reader

+ `[<emitter>, ...]` : List of emitters. Empty if there is no emitter.
  + `<emitter>` : Hostname of the emitter
+ `[<metric>: <value>, ...]` : List of values for the requested emitter. The list limit is 10 values and can be empty if the emitter does not exist.
  + `<metric>` : Type of the data sent (CPU, RAM, DSK)
  + `<value>` : Value of the sent data
    + cpu : CPU consumption in percentage
    + ram : RAM consumption in MB
    + dsk : Disk consumption in MB
+ `ERROR <ErrorNum>` : Send an error to the reader
  + `<ErrorNum>` : Error number
    + `1` : The message could not be parsed

#pagebreak()

== Examples

The next figure contains an example of a couple readers that may be sending messages in parallel to the server. The server will then respond to each reader with the requested information. 

#align(center)[
  #image("protocol-unicast.svg", width: 50%)
]
