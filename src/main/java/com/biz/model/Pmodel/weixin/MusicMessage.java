package com.biz.model.Pmodel.weixin;

/**
 * 响应消息之音乐消息
 * @author GengLong
 *
 */
public class MusicMessage extends BaseMessage
{
	/**
	 * 音乐
	 */
	private Music Music;

	public Music getMusic()
	{
		return Music;
	}

	public void setMusic(Music music)
	{
		Music = music;
	}

}
