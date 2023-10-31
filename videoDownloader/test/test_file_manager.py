import pytest

from service.file_manager import FileManager
from unittest import TestCase


class FileManagerTest(TestCase):
    @pytest.fixture(autouse=True)
    def fixture(self):
        self.filemanager = FileManager()
        self.sample_video_name = "sample_video[1234]"

    def test_video_file_exists__returns_True__file_exists(self):
        # given

        # when

        # then
        self.assertTrue(self.filemanager.video_file_exists(self.sample_video_name))

    def test_video_file_exists__returns_False__file_not_exists(self):
        # given

        # when

        # then
        self.assertFalse(self.filemanager.video_file_exists("INVALID"))
