import os
import pytest

from urllib import parse

from service.s3_manager import S3Manager
from unittest import TestCase


class S3ManagerTest(TestCase):
    @pytest.fixture(autouse=True)
    def fixture(self):
        self.s3_manager = S3Manager()
        self.sample_video_dir = os.path.join(
            os.path.dirname(os.path.abspath(__file__)), "../temp"
        )
        self.sample_video_name = "sample_video[1234].mp4"

    def test_s3_file_exists_returns(self):
        # given

        # when
        exists = self.s3_manager.s3_file_exists(self.sample_video_name)

        # then
        assert exists is True

    def test_s3_file_exists_returns_False(self):
        # given

        # when
        exists = self.s3_manager.s3_file_exists(self.sample_video_name)

        # then
        assert exists is False

    def test_init_check_s3_credentials_by_list_buckets(self):
        # given

        # when
        s3_client = self.s3_manager.s3_client
        list_buckets = s3_client.list_buckets()

        # then
        assert S3Manager.BUCKET_NAME in list(
            map(lambda x: x["Name"], list_buckets["Buckets"])
        )

    def test_upload_video_sample_file(self):
        # given
        sample_video_full_path = os.path.join(
            self.sample_video_dir, self.sample_video_name
        )

        # when
        self.s3_manager.upload_video(sample_video_full_path, self.sample_video_name)
        sample_video_url = self.s3_manager.get_video_pre_signed_url(
            self.sample_video_name
        )

        # then
        # assert string with URL encode("sample_video%5B1234%5D.mp4")
        assert parse.quote(self.sample_video_name) in sample_video_url
