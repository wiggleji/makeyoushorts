from service.video_downloader import VideoDownloader
from service.file_manager import FileManager


class VideoService:
    def __init__(self) -> None:
        self.video_downloader = VideoDownloader()
        self.file_manager = FileManager()

    def download_video(self, video_url: str, video_options: dict):
        """비디오 다운로드

        :param str video_url: 비디오 URL
        :param dict video_options: YoutubeDL 에 사용될 비디오 옵션
            - start: 영상 시작 초
            - end: 영상 종료 초
        :return bool: 비디오 다운로드 성공 여부
        """
        # get Youtube video info
        video_info_json = self.video_downloader.get_video_info_from_youtube(video_url)

        if self.video_downloader.supports_mp4_format(video_info_json) is False:
            # return if video does not support mp4 format
            return False

        # setup options for video download
        options_setup = self.video_downloader.set_yt_dlp_options(video_options)
        if options_setup:
            # download service
            self.video_downloader.download_video_from_youtube(video_url)
            return self.is_video_download_succeed(video_info_json, video_options)
        else:
            # log error
            # throw exception for bad request
            return False

    def is_video_download_succeed(self, video_info_json: dict, video_options: dict):
        """비디오 다운로드 성공 여부
        비디오 파일 여부 확인

        :param dict video_info_json: YoutubeDL 로 조회한 비디오 정보 dict
        :param dict video_options: 비디오 다운로드 옵션(start, end)
        :return bool: 비디오 다운로드 성공 여부
        """
        video_filename = self.video_downloader.default_video_filename(
            video_info_json, video_options
        )
        return self.file_manager.video_file_exists(video_filename)

    def download_video_file_w_full_path(self, video_url: str, video_options: dict):
        """비디오 다운로드 정보 반환
        :param str video_url: 비디오 URL
        :param dict video_options: 비디오 다운로드 옵션(start, end)
        :return (str, str): 비디오 파일 fullpath, 비디오 파일명
        """
        video_info_json = self.video_downloader.get_video_info_from_youtube(video_url)
        video_filename = self.video_downloader.default_video_filename(
            video_info_json, video_options
        )
        return (
            self.file_manager.video_file_full_path(video_filename),
            f"{video_filename}.mp4",
        )
