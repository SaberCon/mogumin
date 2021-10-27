package cn.sabercon.megumin.image

import cn.sabercon.common.data.AssetRepository
import cn.sabercon.common.util.EPOCH
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

interface ImageRepo : AssetRepository<Image>

@Document
data class Image(
    @Id val id: String = "",
    val userId: Long,
    val name: String,
    val url: String,
    val size: String,
    val ctime: LocalDateTime = EPOCH,
    val mtime: LocalDateTime = EPOCH,
)