from typing import Optional, Literal

from fastapi import FastAPI, Depends, status, HTTPException
from fastapi.responses import JSONResponse
from pydantic import BaseModel

from service.video_service import VideoService
from service.s3_manager import S3Manager

app = FastAPI()


class VideoRequest(BaseModel):
    """
    비디오 다운로드 Request DTO
    """

    video_url: str
    start: int
    end: int


class VideoResponse(BaseModel):
    """
    비디오 다운로드 Response DTO
    """

    file_name: str
    start: int
    end: int


@app.post(
    "/video/download",
    summary="Download Youtube video URL",
    description="get Youtube video file with time range options:{start, end}",
    response_model=VideoResponse,
    status_code=status.HTTP_202_ACCEPTED,
)
def download_video_file(
    video_request: VideoRequest,
    video_service: VideoService = Depends(VideoService),
    s3_manager: S3Manager = Depends(S3Manager),
):
    video_url = youtube_url_resolver(video_request.video_url)
    if video_url is None:
        return None
    video_download_succeed = video_service.download_video(
        video_url, options={"start": video_request.start, "end": video_request.end}
    )
    if video_download_succeed:
        video_file_full_path, file_name = video_service.download_video_file_w_full_path(
            video_url
        )
        s3_manager.upload_video(video_file_full_path, file_name)

        return VideoResponse(
            file_name=file_name,
            start=video_request.start,
            end=video_request.end,
        )
    else:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="Item not found"
        )


def youtube_url_resolver(youtube_url: str) -> Optional[str]:
    """YouTube URL resolver: URL 획일화
    # https://www.youtube.com/watch?v=v6gCRbugjiU
    # https://youtu.be/v6gCRbugjiU?si=G0c2DS7P7eBW7fA8
    -> 비디오 ID(v6gCRbugjiU) 반환

    :param str youtube_url: YouTube 비디오 URL [youtube.com / youtu.be]
    :return: str: Youtube 비디오 URL [youtu.be]
    """
    try:
        if "youtube.com" in youtube_url:
            video_id = youtube_url.split("watch?v=")[-1]
        elif "youtu.be" in youtube_url:
            video_id = youtube_url.split("/")[-1]
        else:
            return None
        return f"https://youtu.be/{video_id}"
    except IndexError:
        # return None if ID parsing fail
        return None
    except Exception as e:
        # log unhandled exception
        return None
