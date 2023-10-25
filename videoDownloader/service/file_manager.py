import os


class FileManager:
    # output: videoDownloader/temp
    DEFAULT_VIDEO_FILE_TEMP_PATH = os.path.join(
        os.path.dirname(os.path.abspath(__file__)), "../temp"
    )

    def video_file_exists(self, filename):
        """비디오 파일 존재 여부

        :param str filename: 비디오 파일명
        :return bool: 파일 존재 여부
        """
        file_exists = os.path.isfile(
            os.path.join(self.DEFAULT_VIDEO_FILE_TEMP_PATH, f"{filename}.mp4")
        )
        return file_exists

    def video_file_full_path(self, filename):
        """비디오 파일 path 반환

        :param str filename: 비디오 파일명
        :return str: 비디오 파일 path
        """
        if self.video_file_exists(filename):
            return os.path.join(self.DEFAULT_VIDEO_FILE_TEMP_PATH, f"{filename}.mp4")
        return None

    # TODO: temp 파일 주기성 삭제 처리 시 사용
    #  use to delete temp files on server with celery worker
    def delete_video_file(self, filename):
        """비디오 파일 삭제 (주기적으로 처리)

        :param str filename: 비디오 파일명
        :return bool: 비디오 파일 삭제 여부
        """
        if self.video_file_exists(filename):
            os.remove(
                os.path.join(self.DEFAULT_VIDEO_FILE_TEMP_PATH, f"{filename}.mp4")
            )
            return True
        return False
