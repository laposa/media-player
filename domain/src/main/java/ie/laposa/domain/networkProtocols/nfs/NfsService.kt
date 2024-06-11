package ie.laposa.domain.networkProtocols.nfs

import ie.laposa.domain.mediaSource.model.MediaSource
import org.apache.commons.vfs2.FileObject
import org.apache.commons.vfs2.VFS
import java.net.URI


class NfsService {
    fun connect(
        mediaSource: MediaSource,
    ) {
        val fsManager = VFS.getManager()


        // Construct the NFS URL
        val nfsUrl =
            URI.create("${mediaSource.connectionAddress}/Users/tomaspolak/Movies/GoPro/test.MP4")


        // Resolve the file object
        val nfsFile = fsManager.resolveFile(nfsUrl)


        // Example operation: List files in the directory
        if (nfsFile.exists() && nfsFile.isFolder()) {
            val children: Array<FileObject> = nfsFile.getChildren()
            System.out.println("Children of " + nfsFile.getName().getURI())
            for (child in children) {
                println(child.name.baseName)
            }
        } else {
            println("NFS share does not exist or is not a directory")
        }
    }


    private fun log(message: String) = println("$TAG: $message")

    companion object {
        const val TAG = "NfsService"
    }
}