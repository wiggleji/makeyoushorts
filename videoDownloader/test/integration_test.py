from unittest import TestCase

import pytest

from service.video_service import VideoService


class IntegrationTest(TestCase):
    @pytest.fixture(autouse=True)
    def fixture(self):
        self.video_service = VideoService()
        self.video_url = "https://youtu.be/Gr-BGf7rzrY"
        self.options = {"start": 10, "end": 20}

    def test_download_integration_test(self):
        # download integration test: "WOWOW×Vaundy Museum Live on YouTube" video
        download_result = self.video_service.download_video(
            self.video_url, self.options
        )

        self.assertTrue(download_result)
