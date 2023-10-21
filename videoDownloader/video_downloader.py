import yt_dlp

from file_manager import FileManager
from logger import Logger


class VideoDownloader:
    def __init__(self) -> None:
        self.file_manager = FileManager()
        self.yt_dlp_options = None

    @staticmethod
    def supports_mp4_format(video_info_json):
        """YoutubeDL 영상의 mp4 파일 지원 여부

        :param dict video_info_json: YoutubeDL 로 조회한 비디오 정보 dict
        :return bool: mp4 파일 지원 여부
        """
        try:
            video_support_formats = set(
                map(lambda x: x["ext"], video_info_json["formats"])
            )
            return "mp4" in video_support_formats
        except KeyError:
            return False

    @staticmethod
    def default_video_filename(video_info_json):
        """비디오 파일 default 이름
        default filename: {video_info_json['fulltitle']} [{video_info_json['id']}].mp4

        :param dict video_info_json: YoutubeDL 로 조회한 비디오 정보 dict
        :return str: 기본 비디오 파일(.mp4) 이름
        """
        try:
            return f"{video_info_json['fulltitle']} [{video_info_json['id']}]"
        except KeyError:
            return None

    def set_yt_dlp_options(self, options=None):
        """YoutubeDL 세팅

        :param dict options: 비디오 다운로드 옵션 (시작/종료 초 등)
        :return bool: YoutubeDL options 설정 여부
        """
        self.yt_dlp_options = {
            "paths": {
                "temp": self.file_manager.DEFAULT_VIDEO_FILE_TEMP_PATH,
                "home": self.file_manager.DEFAULT_VIDEO_FILE_TEMP_PATH,
            },
            "verbose": True,
            "format": "best",
            "logger": Logger(),
            "progress_hooks": [Logger.hook],
        }

        # options {"start", "end"} 설정
        try:
            start = options["start"]
            end = options["end"]
            download_ranges = yt_dlp.utils.download_range_func(None, [(start, end)])
        except (TypeError, KeyError) as e:
            # error handling for options param
            return False
        if download_ranges:
            self.yt_dlp_options["download_ranges"] = download_ranges

        return True

    def get_video_info_from_youtube(self, video_url: str):
        """YoutubeDL 로 유튜브 비디오 정보 조회

        :param str video_url: 비디오 URL
        :return: dict: 비디오 정보
        """
        with yt_dlp.YoutubeDL(self.yt_dlp_options) as ydl:
            video_info = ydl.extract_info(video_url, download=False)
            return ydl.sanitize_info(video_info)

    def download_video_from_youtube(self, video_url: str):
        """YoutubeDL 로 유튜브 비디오 다운로드

        :param str video_url: 비디오 URL
        :return int: 비디오 다운로드 성공 여부(_download_retcode)
        """
        with yt_dlp.YoutubeDL(self.yt_dlp_options) as ydl:
            return ydl.download([video_url])
