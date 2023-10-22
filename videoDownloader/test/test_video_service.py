import pytest

from unittest import TestCase
from unittest.mock import patch

from test import test_fixture
from service.video_service import VideoService


class VideoServiceTest(TestCase):
    @pytest.fixture(autouse=True)
    def fixture(self):
        self.video_service = VideoService()
        self.video_url = "https://youtu.be/Gr-BGf7rzrY"
        self.options = {"start": 10, "end": 20}

    @patch("service.video_service.VideoDownloader.get_video_info_from_youtube")
    @patch("service.video_service.VideoDownloader.download_video_from_youtube")
    def test_download_video__success(
        self, mock_download_video_from_youtube, mock_get_video_info_from_youtube
    ):
        # given
        # mocking VideoDownloader.get_video_info_from_youtube with MP4 format
        mock_get_video_info_from_youtube.return_value = (
            test_fixture.FIXTURE_MP4_VIDEO_INFO_JSON
        )
        # mocking VideoDownloader.download_video_from_youtube
        mock_download_video_from_youtube.return_value = True

        # when
        video_download_result = self.video_service.download_video(
            self.video_url, self.options
        )

        # then
        self.assertTrue(video_download_result)

    @patch("service.video_service.VideoDownloader.get_video_info_from_youtube")
    def test_download_video__fail__does_not_support_mp4(
        self, mock_get_video_info_from_youtube
    ):
        # given
        # mocking VideoDownloader.get_video_info_from_youtube without MP4 format
        mock_get_video_info_from_youtube.return_value = (
            test_fixture.FIXTURE_NON_MP4_VIDEO_INFO_JSON
        )

        video_url = "https://youtu.be/Gr-BGf7rzrY"
        options = {"start": 10, "end": 20}

        # when
        video_download_result = self.video_service.download_video(
            self.video_url, self.options
        )

        # then
        self.assertFalse(video_download_result)

    @patch("service.video_service.VideoDownloader.get_video_info_from_youtube")
    def test_download_video__fail__invalid_options(
        self, mock_get_video_info_from_youtube
    ):
        # given
        # mocking VideoDownloader.get_video_info_from_youtube with MP4 format
        mock_get_video_info_from_youtube.return_value = (
            test_fixture.FIXTURE_MP4_VIDEO_INFO_JSON
        )

        invalid_options = {"INVALID": "false"}

        # when
        video_download_result = self.video_service.download_video(
            self.video_url, invalid_options
        )

        # then
        self.assertFalse(video_download_result)

    def test_is_video_download_succeed__return_True(self):
        # given

        # when
        download_succeed = self.video_service.is_video_download_succeed(
            test_fixture.FIXTURE_MP4_VIDEO_INFO_JSON
        )

        # then
        self.assertTrue(download_succeed)

    def test_is_video_download_succeed__return_False(self):
        # given
        invalid_video_info_json = {"INVALID": "false"}

        # when
        download_succeed = self.video_service.is_video_download_succeed(
            invalid_video_info_json
        )

        # then
        self.assertFalse(download_succeed)
