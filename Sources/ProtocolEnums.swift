enum PacketIdentifier: UInt8 {
	case PING      = 0x25
	case AUTH_INFO = 0x50
}


enum ProtocolPlatform: UInt32 {

	case Windows      = 0x49583836 // 'IX86'
	case MacOS9       = 0x504d4143 // 'PMAC'
	case MacOS10      = 0x584d4143 // 'XMAC'
	case MacOS10Intel = 0x4f535849 // 'OSXI'

}


enum ProtocolGame: UInt32 {

	case StarcraftShareware = 0x53534852 // 'SSHR'
	case StarcraftJapan     = 0x4a535452 // 'JSTR'
	case StarcraftRetail    = 0x53544152 // 'STAR'
	case StarcraftExpansion = 0x53455850 // 'SEXP'

	case DiabloShareware    = 0x44534852 // 'DSHR'
	case DiabloRetail       = 0x4452544c // 'DRTL'

	case Diablo2	         = 0x44324456 // 'D2DV'
	case Diablo2Expansion   = 0x44325850 // 'D2XP'

	case Warcraft2          = 0x5732424e // 'W2BN'
	case Warcraft3          = 0x57415233 // 'WAR3'
	case Warcraft3Expansion = 0x57335850 // 'W3XP'

}
