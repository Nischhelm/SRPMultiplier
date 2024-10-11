SRPMultiplier

Changes some stuff in SRParasites to work better with RLCraft

Changes:
- Global Multipliers for Health, Armor, Damage and KB Resistance are set per dimension (default is 1x for overworld, 2x for nether+end, 4x for LC)
- Additional Multiplier increasing with Phase (default disabled)
- LC starts in Phase 8, Stage 4 Beckons and Stage 3 Dispatchers spawn naturally, Lures are disabled and turn to Dispatcher Nidus when trying to use them
- LC Portal only enabled from Phase 6, unlock Preeminents and Beckon+Dispatcher spawns everywhere from Phase 6 after entering
- Bloody Clock also shows the progress to next phase in %
- Parasite Spawners in LC buildings work
- Plays respective sounds when dispatchers or beckons of higher stages naturally spawn
- Bloodmoon in LC (increased Parasite Mob Cap + increased Parasite spawning speed)
- Strange Bones stack to 16
- Buff Assimilated Endermen to tp Primitive, Adapted and Pure mobs as well (depending on phase)
- Nerf End Assimilated Endermen to stop the utter chaos
  - Added custom mob cap for Endermen turning to Assimilated Endermen in the end
  - Made Assimilated Endermen always be able to despawn in End, even when coming from COTH
- Cap Nexus Type spawns to 15 (custom mob cap)
- Added whitelist for Nexus/Deterrent types to not take dmg over time in too low phases
- Fix Sentient Armor Viral Limiting
- Fixed Multiplayer Sleeping Penalty being counted per sleeping player
- Fix Lycanite Spawner jsons not picking up srp mobs without reload
- Made Lures have phase dependent multipliers for their point reduction value
- Each player can have their own phases, making late joins in multiplayer less bad

Idea is that LC is overrun by parasites via the End. Now they spill over to the other dimensions. If you try to enter LC, theres gonna be parasitic biomes almost immediately and you bring home even worse nightmares.
	
Built upon FermiumTemplateMod by FonnyMunkey, needs FermiumBooter
https://github.com/fonnymunkey/FermiumTemplateMod/
