# theo-instrument -- play some sounds with supercollider looped and continuously

Send osc commands:

* /noteon i where i is the 0-indexed sound file to play
* /noteoff i where i is the 0-indexed sound file to NOT play
* /set i_0,i_1,...,i_{n-1},i_n where i_0 are the gate values of each soundfile to play

Typically it will be on UDP 127.0.0.1 port 57120

This script can be invoked directly with sclang

# Copyright / License

Copyright 2017 (c) Abram Hindle

Licensed under Apache 2 license.

