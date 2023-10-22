import json
import os

from typing_extensions import deprecated


class FileManager:
    # output: videoDownloader/temp
    DEFAULT_VIDEO_FILE_TEMP_PATH = os.path.join(
        os.path.dirname(os.path.abspath(__file__)), "../temp"
    )

    def video_file_exists(self, filename):
        file_exists = os.path.isfile(
            os.path.join(self.DEFAULT_VIDEO_FILE_TEMP_PATH, f"{filename}.mp4")
        )
        return file_exists

    @deprecated("use to delete temp files on server with celery worker")
    def delete_video_file(self, filename):
        if self.video_file_exists(filename):
            os.remove(
                os.path.join(self.DEFAULT_VIDEO_FILE_TEMP_PATH, f"{filename}.mp4")
            )
            return True
        return False
