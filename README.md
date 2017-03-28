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

   Copyright 2017 Abram Hindle

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
