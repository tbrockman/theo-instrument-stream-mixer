// Copyright 2017 (c) Abram Hindle
// Licensed under Apache 2 license.
//
// Send osc commands:
//     /noteon i where i is the 0-indexed sound file to play
//     /noteoff i where i is the 0-indexed sound file to NOT play
//     /set i_0,i_1,...,i_{n-1},i_n where i_0 are the gate values of each soundfile to play
// Typically it will be on UDP 127.0.0.1 port 57120
//
// This script can be invoked directly with sclang
s.boot;

s.waitForBoot({
	~waves = [
		"/home/theodore/Documents/cake/audio/Angelic Ghosts_1.aif",
		"/home/theodore/Documents/cake/audio/Sample0022.wav",
		"/home/theodore/Documents/cake/audio/Sample0033.wav",
		"/home/theodore/Documents/cake/audio/Sample0044.wav",
		"/home/theodore/Documents/cake/audio/Sample0055.wav",
		"/home/theodore/Documents/cake/audio/Sample0066.wav",
		"/home/theodore/Documents/cake/audio/Sample0077.wav",
		"/home/theodore/Documents/cake/audio/Sample0088.wav",
	];

	~loadbuff = { |files|
		files.collect({|x| Buffer.read(s,x) });
	};
	~sounds = ~loadbuff.(~waves);

	SynthDef(\playBuffer, {| out = #[0,1], bufnum = 0, amp = 0, gate = 0, attack = 2.5, release = 5 |
		var adsr = Env.adsr (attackTime: attack, decayTime: 0.3, sustainLevel: 0.5, releaseTime: release, peakLevel: 1, curve: -4, bias: 0),
		env  = EnvGen.kr(adsr, gate, doneAction: 0);
		Out.ar(out,
			env*amp*PlayBuf.ar(1, bufnum, BufRateScale.kr(bufnum), 1, 0, 0, 1)
		)
	}).load(s);
	s.sync;
	~synths = ~sounds.collect {|buf| Synth(\playBuffer,[\out,[0,1],\bufnum,buf,\amp,0.5]) };

	// ~synths.do {|synth| synth.set(\amp,0.25) }

	// trigger the synths:
	// ~synths.do {|synth| synth.set(\gate,1) }
	// untrigger the synths;
	// ~synths.do {|synth| synth.set(\gate,0) }
	~noteon = {
		|msg|
		var id = msg[1];
		[msg].postln;
		if((id < ~synths.size) && (id >= 0), {
			~synths[id].set(\gate,1.0);
			["turning on",id].postln;
		});
	};
	~noteoff = {
		|msg|
		var id = msg[1];
		if((id < ~synths.size) && (id >= 0), {
			~synths[id].set(\gate,0.0);
			["turning off",id].postln;
		});
	};
	~statehandler = {
		|msg|
		// for all message args set the gate
		msg[1..].do {|v,id|
			id = id - 1;
			if((id < ~synths.size) && (id >= 0), {
				~synths[id].set(\gate,v);
				["setting",id,v].postln;
			});
		};
	};
	OSCFunc.newMatching(~noteon, '/noteon');
	OSCFunc.newMatching(~noteoff, '/noteoff');
	OSCFunc.newMatching(~statehandler, '/set');

/*	MIDIClient.init;
	MIDIIn.connectAll;
	MIDIdef.noteOn(\noteOn, {
		arg vel, note, chan, src;
		var n = NetAddr.localAddr;
		[vel, note, note.asInteger - 65].postln;
		n.sendMsg("/noteon", note.asInteger - 65)
	});

	MIDIdef.noteOff(\noteOff, {
		arg vel, note, chan, src;
		var n = NetAddr.localAddr;
		var index = note.asInteger - 65;
		[vel, note, index].postln;
		n.sendMsg("/noteoff", index)
	});*/

	~tester = Routine({
		var n = NetAddr.localAddr;
		n.sendMsg("/set",1,1,1,1,1,1,1,1);
		~synths.size.do {|i|
			1.0.wait;
			n.sendMsg("/noteoff",i);
		};
		~synths.size.do {|i|
			1.0.wait;
			n.sendMsg("/noteon",i);
		};
		3.0.wait;
		~synths.size.do {|i|
			1.0.wait;
			n.sendMsg("/noteoff",i);
		};
	});
});
// give it a whirl
~tester.play;