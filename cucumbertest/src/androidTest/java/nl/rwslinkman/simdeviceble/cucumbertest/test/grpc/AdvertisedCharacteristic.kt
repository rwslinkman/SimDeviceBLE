package nl.rwslinkman.simdeviceble.cucumbertest.test.grpc

data class AdvertisedCharacteristic(
    val name: String,
    val uuid: String,
    val currentValue: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdvertisedCharacteristic

        if (name != other.name) return false
        if (uuid != other.uuid) return false
        if (!currentValue.contentEquals(other.currentValue)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + uuid.hashCode()
        result = 31 * result + currentValue.contentHashCode()
        return result
    }
}
