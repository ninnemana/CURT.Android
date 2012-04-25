/**
 * 
 */
package curt.android.camera;

import java.io.File;

/**
 * @author alexninneman
 *
 */
public abstract class AlbumStorageDirFactory {
	public abstract File getAlbumStorageDir(String albumName);
}
