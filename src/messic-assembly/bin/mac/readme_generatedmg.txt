To generate the mac installer (dmg), you must:

- Go to Applications->tools->disk utility
- create a new image, and put there the name of the installer (messic)
- you must assign enough space to the image (200 MB should be fine)
- let it as a read/write disk image
- Press create

- After that, the image will be created and mounted. At the blank folder, right-click and select showViewOptioins from menu
- drop at the blank folder the background of the installer
- select the option "picture" at the view options of the folder and drop there the image dropped at the blank folder
- open a terminal console, cd /Volumnes/<thenameofyourimage>/ and rename the file image dropped to hide it (i.e: mv imagedroped.jpeg .imagedropped.jpeg)
- change the size of icons to 104 for example
- drop at the blank page (now with the correct background) the messic.app folder generated for mac
- put at the correct position of your background
- copy a link of the applicatons folder to your blank folder at the correct position
- now close the image and unmount

- open again the disk utility, and open the read write image.
- press convert button, and let it as "compressed" (this will change the image to read only.
- save and the new image will be created (read only) ... this should be the correct .dmg installer!