package cn.lingyuncraft.builddreamrecode.utils;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import lombok.NonNull;
import org.bukkit.Location;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WorldEditUtils {

    public static void saveSchematicToFile(@NonNull String publicID, @NonNull CuboidRegion region) throws WorldEditException, IOException {

        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(region.getWorld(), -1)) {
            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
                    editSession, region, clipboard, region.getMinimumPoint()
            );
            Operations.complete(forwardExtentCopy);
        }

        File file = Storage.getSchematicFile(publicID);
        File folder = new File(Storage.getStorageFolder().getPath() + File.separator + publicID);
        folder.mkdir();
        file.createNewFile();
        try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(file))) {
            writer.write(clipboard);
        }
    }

    public static Clipboard getSchematicFromFile(@NonNull String publicID) {
        File file = Storage.getSchematicFile(publicID);
        Clipboard clipboard = null;
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            clipboard = reader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clipboard;
    }

    public static void pasteSchematic(@NonNull String publicID, @NonNull Location loc) {
        com.sk89q.worldedit.world.World WEWorld = BukkitAdapter.adapt(loc.getWorld());
        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(WEWorld, -1)) {
            Operation operation = new ClipboardHolder(getSchematicFromFile(publicID))
                    .createPaste(editSession)
                    .to(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()))
                    .ignoreAirBlocks(true)
                    .copyBiomes(false)
                    .copyEntities(false)
                    .build();
            Operations.complete(operation);

        } catch (WorldEditException e) {
            e.printStackTrace();
        }
    }
}
