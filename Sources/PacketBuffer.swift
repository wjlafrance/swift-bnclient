import Foundation

struct PacketBuffer: CustomDebugStringConvertible {

	var contents: [UInt8] = []

	mutating func append(data: UInt8) {
		contents.append(data)
	}
	mutating func append(data: UInt16) {
		append(UInt8(data >> 8 & 0xFF))
		append(UInt8(data >> 0 & 0xFF))
	}
	mutating func append(data: UInt32) {
		append(UInt16(data >> 16 & 0xFFFF))
		append(UInt16(data >>  0 & 0xFFFF))
	}
	mutating func append(data: UInt64) {
		append(UInt32(data >> 32 & 0xFFFFFFFF))
		append(UInt32(data >>  0 & 0xFFFFFFFF))
	}

	mutating func append(data: [UInt8]) {
		contents.appendContentsOf(data)
	}

	mutating func readUInt8() -> UInt8 {
		return contents.removeFirst()
	}
	mutating func readUInt16() -> UInt16 {
		return UInt16(readUInt8()) << 8 | UInt16(readUInt8())
	}
	mutating func readUInt32() -> UInt32 {
		return UInt32(readUInt16()) << 16 | UInt32(readUInt16())
	}
	mutating func readUInt64() -> UInt64 {
		return UInt64(readUInt32()) << 32 | UInt64(readUInt32())
	}

	mutating func addBncsHeader(packetId: UInt8) {
		var header = PacketBuffer()
		header.append(0xFF as UInt8)
		header.append(packetId)
		header.append(UInt16(contents.count) + 4)
		header.append(contents)
		self = header
	}

	var debugDescription: String {
		let lineLength = 16
		return ((0...(contents.count / lineLength)).map { lineIndex in
			return String(format: "%04X: ", lineIndex) + ((0...lineLength).flatMap { columnIndex in
				let arrayIndex = lineIndex * 16 + columnIndex
				if arrayIndex >= contents.count { return nil }
				return String(format: "%02X ", contents[arrayIndex])
			}).joinWithSeparator("")
		}).joinWithSeparator("\n")
	}

}
