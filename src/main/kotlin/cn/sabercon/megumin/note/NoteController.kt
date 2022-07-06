package cn.sabercon.megumin.note

import cn.sabercon.common.graphql.CountConnection
import cn.sabercon.common.graphql.CreatePayload
import cn.sabercon.common.graphql.DefaultPayload
import cn.sabercon.common.graphql.ForwardPagination
import cn.sabercon.common.graphql.UpdatePayload
import cn.sabercon.dgs.codegen.generated.types.CreateNoteInput
import cn.sabercon.dgs.codegen.generated.types.DeleteNoteInput
import cn.sabercon.dgs.codegen.generated.types.UpdateNoteInput
import cn.sabercon.megumin.user.User
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.Arguments
import org.springframework.graphql.data.method.annotation.ContextValue
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class NoteController(private val service: NoteService) {

    @MutationMapping
    suspend fun createNote(@ContextValue userId: Long, @Argument input: CreateNoteInput): CreatePayload<Note> {
        val note = service.create(userId, input)
        return CreatePayload(input.clientMutationId, note)
    }

    @MutationMapping
    suspend fun updateNote(@ContextValue userId: Long, @Argument input: UpdateNoteInput): UpdatePayload<Note> {
        val note = service.update(userId, input)
        return UpdatePayload(input.clientMutationId, note)
    }

    @MutationMapping
    suspend fun deleteNote(@ContextValue userId: Long, @Argument input: DeleteNoteInput): DefaultPayload {
        service.delete(userId, input.id)
        return DefaultPayload(input.clientMutationId)
    }

    @SchemaMapping
    suspend fun notes(viewer: User, @Arguments pagination: ForwardPagination): CountConnection<Note> {
        val page = service.getPage(viewer.id, pagination.toPageRequest())
        return CountConnection.fromForwardPage(page)
    }
}
