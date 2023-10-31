import pytest

from unittest import TestCase

from test import test_fixture
from service.video_downloader import VideoDownloader


class FileManagerTest(TestCase):
    @pytest.fixture(autouse=True)
    def fixture(self):
        self.video_downloader = VideoDownloader()
        self.wrong_video_info_json = {"WRONG_VALUE": "false"}

    def test__static_supports_mp4_format__return_True(self):
        # given
        video_info_json = self.video_downloader.get_video_info_from_youtube(
            test_fixture.FIXTURE_VIDEO_URL
        )

        # when
        supports_mp4 = self.video_downloader.supports_mp4_format(video_info_json)

        # then
        self.assertTrue(supports_mp4)

    def test__static_supports_mp4_format__return_False(self):
        # given

        # when
        supports_mp4 = self.video_downloader.supports_mp4_format(
            self.wrong_video_info_json
        )

        # then
        self.assertFalse(supports_mp4)

    def test__default_video_filename__return_filename(self):
        # given
        assertion_filename = "WOWOW×Vaundy Museum Live on YouTube[Gr-BGf7rzrY]"

        # when
        default_video_filename = self.video_downloader.default_video_filename(
            test_fixture.FIXTURE_MP4_VIDEO_INFO_JSON
        )

        # then
        self.assertEqual(default_video_filename, assertion_filename)

    def test__default_video_filename__return_None(self):
        # given

        # when
        default_video_filename = self.video_downloader.default_video_filename(
            self.wrong_video_info_json
        )

        # then
        self.assertIsNone(default_video_filename)

    def test__yt_dlp_options__setup_success(self):
        # given
        options = {"start": 10, "end": 20}

        # when
        options_status = self.video_downloader.set_yt_dlp_options(options)

        # then
        self.assertTrue(options_status)
        self.assertEqual(
            self.video_downloader.yt_dlp_options["download_ranges"].ranges,
            [(options["start"], options["end"])],
        )

    def test__yt_dlp_options__setup_fail__name_error_handling(self):
        # given

        # when
        options_status = self.video_downloader.set_yt_dlp_options()

        # then
        self.assertFalse(options_status)
        self.assertIsNone(self.video_downloader.yt_dlp_options.get("download_ranges"))

    def test__yt_dlp_options__setup_fail__type_error_handling(self):
        # given
        options = {}

        # when
        options_status = self.video_downloader.set_yt_dlp_options(options)

        # then
        self.assertFalse(options_status)
        self.assertIsNone(self.video_downloader.yt_dlp_options.get("download_ranges"))
