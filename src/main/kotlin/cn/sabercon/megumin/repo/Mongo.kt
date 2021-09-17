package cn.sabercon.megumin.repo

import cn.sabercon.common.data.AssetRepository
import cn.sabercon.megumin.model.File
import cn.sabercon.megumin.model.Image
import cn.sabercon.megumin.model.Note
import cn.sabercon.megumin.model.Password

interface NoteRepo : AssetRepository<Note>

interface ImageRepo : AssetRepository<Image>

interface FileRepo : AssetRepository<File>

interface PasswordRepo : AssetRepository<Password>