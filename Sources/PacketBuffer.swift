import Foundation

extension NSData {
	convenience init(packetBuffer: PacketBuffer) {
		var contents = packetBuffer.contents
		self.init(bytes: &contents, length: contents.count)
	}
}

struct PacketBuffer: CustomDebugStringConvertible {

	var contents: [UInt8] = []

	init() {
	}

	init(data: NSData) {
		for _ in 0..<data.length { append(0 as UInt8) }
		data.getBytes(&contents, length: data.length)
	}

	mutating func append(data: UInt8) {
		contents.append(data)
	}
	mutating func append(data: UInt16) {
		append(UInt8(data >> 0 & 0xFF))
		append(UInt8(data >> 8 & 0xFF))
	}
	mutating func append(data: UInt32) {
		append(UInt16(data >>  0 & 0xFFFF))
		append(UInt16(data >> 16 & 0xFFFF))
	}
	mutating func append(data: UInt64) {
		append(UInt32(data >>  0 & 0xFFFFFFFF))
		append(UInt32(data >> 32 & 0xFFFFFFFF))
	}

	mutating func append(data: String) {
		append(data.nulTerminatedUTF8.map{ $0 })
	}

	mutating func append(data: [UInt8]) {
		contents.appendContentsOf(data)
	}

	mutating func readUInt8() -> UInt8 {
		return contents.removeFirst()
	}
	mutating func readUInt16() -> UInt16 {
		return UInt16(readUInt8()) | UInt16(readUInt8()) << 8
	}
	mutating func readUInt32() -> UInt32 {
		return UInt32(readUInt16()) | UInt32(readUInt16()) << 16
	}
	mutating func readUInt64() -> UInt64 {
		return UInt64(readUInt32()) | UInt64(readUInt32()) << 32
	}

	mutating func addBncsHeader(packetIdentifier: PacketIdentifier) {
		var header = PacketBuffer()
		header.append(0xFF as UInt8)
		header.append(packetIdentifier.rawValue)
		header.append(UInt16(contents.count) + 4)
		header.append(contents)
		self = header
	}

	var debugDescription: String {
		let lineLength = 16
		return ((0...(contents.count / lineLength)).map { lineIndex in
			let hexPrintout = ((0..<lineLength).flatMap { columnIndex in
				let arrayIndex = lineIndex * 16 + columnIndex
				if arrayIndex >= contents.count { return nil }
				return String(format: "%02X ", contents[arrayIndex])
			}).joinWithSeparator("")
			let charPrintout = ((0..<lineLength).flatMap { columnIndex in
				let arrayIndex = lineIndex * 16 + columnIndex
				if arrayIndex >= contents.count { return nil }
				if isprint(Int32(contents[arrayIndex])) == 0 { return "." }
				return String(format: "%c", contents[arrayIndex])
			}).joinWithSeparator("")

			return String(format: "%04X: ", lineIndex) + "\(hexPrintout)   \(charPrintout)"
		}).joinWithSeparator("\n")
	}

}
