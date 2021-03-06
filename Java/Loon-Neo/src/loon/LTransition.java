/**
 * Copyright 2008 - 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loon
 * @author cping
 * @email：javachenpeng@yahoo.com
 * @version 0.1
 */
package loon;

import loon.action.map.Config;
import loon.action.sprite.ISprite;
import loon.action.sprite.effect.ArcEffect;
import loon.action.sprite.effect.CrossEffect;
import loon.action.sprite.effect.FadeDotEffect;
import loon.action.sprite.effect.FadeEffect;
import loon.action.sprite.effect.FadeOvalEffect;
import loon.action.sprite.effect.FadeSpiralEffect;
import loon.action.sprite.effect.FadeTileEffect;
import loon.action.sprite.effect.PixelDarkInEffect;
import loon.action.sprite.effect.PixelDarkOutEffect;
import loon.action.sprite.effect.PixelThunderEffect;
import loon.action.sprite.effect.PixelWindEffect;
import loon.action.sprite.effect.SplitEffect;
import loon.canvas.LColor;
import loon.opengl.GLEx;
import loon.opengl.TextureUtils;
import loon.utils.MathUtils;
import loon.utils.TArray;

/**
 * 自0.3.2版起新增的Screen切换过渡效果类，内置有多种过渡特效。
 * 
 * example:
 * 
 * public class Sample extends Screen{
 * 
 * ......
 * 
 * public LTransition onTransition(){ return xxx(method) } }
 * 
 * 
 */
public class LTransition {

	/**
	 * 常用特效枚举列表
	 */
	public enum TransType {
		FadeIn, FadeOut, FadeOvalIn, FadeOvalOut, FadeDotIn, FadeDotOut, FadeTileIn, FadeTileOut, FadeSpiralIn, FadeSpiralOut, PixelDarkIn, PixelDarkOut, CrossRandom, SplitRandom, PixelWind, PixelThunder
	}

	/**
	 * 转换字符串为转换特效的枚举类型
	 * 
	 * @param name
	 * @return
	 */
	public static TransType transStringToType(String name) {
		if (name != null) {
			String key = name.trim().toLowerCase();
			if ("fadein".equals(key)) {
				return TransType.FadeIn;
			} else if ("fadeout".equals(key)) {
				return TransType.FadeOut;
			} else if ("fadeovalin".equals(key)) {
				return TransType.FadeOvalIn;
			} else if ("fadeovalout".equals(key)) {
				return TransType.FadeOvalOut;
			} else if ("fadedotin".equals(key)) {
				return TransType.FadeDotIn;
			} else if ("fadedotout".equals(key)) {
				return TransType.FadeDotOut;
			} else if ("fadetilein".equals(key)) {
				return TransType.FadeTileIn;
			} else if ("fadetileout".equals(key)) {
				return TransType.FadeTileOut;
			} else if ("fadespiralin".equals(key)) {
				return TransType.FadeSpiralIn;
			} else if ("fadespiralout".equals(key)) {
				return TransType.FadeSpiralOut;
			} else if ("pixeldarkin".equals(key)) {
				return TransType.PixelDarkIn;
			} else if ("pixeldarkout".equals(key)) {
				return TransType.PixelDarkOut;
			} else if ("crossrandom".equals(key)) {
				return TransType.CrossRandom;
			} else if ("splitrandom".equals(key)) {
				return TransType.SplitRandom;
			} else if ("pixelwind".equals(key)) {
				return TransType.PixelWind;
			} else if ("pixelthunder".equals(key)) {
				return TransType.PixelThunder;
			} else {
				return TransType.FadeIn;
			}
		}
		return TransType.FadeIn;
	}

	/**
	 * 返回一定指定色彩过滤后的特效
	 * 
	 * @param key
	 * @param c
	 * @return
	 */
	public static LTransition newTransition(String key, LColor c) {
		return newTransition(transStringToType(key), c);
	}

	/**
	 * 返回一定指定色彩过滤后的特效
	 * 
	 * @param key
	 *            过渡类型字符串
	 * @param cs
	 *            描述颜色的字符串
	 * 
	 * @return
	 */
	public static LTransition newTransition(String key, String cs) {
		return newTransition(transStringToType(key), new LColor(cs));
	}

	/**
	 * 返回一定指定色彩过滤后的特效
	 * 
	 * @param t
	 * @param c
	 * @return
	 */
	public static LTransition newTransition(TransType t, LColor c) {
		LTransition transition = null;
		switch (t) {
		default:
		case FadeIn:
			transition = newFadeIn(c);
			break;
		case FadeOut:
			transition = newFadeOut(c);
			break;
		case FadeOvalIn:
			transition = newFadeOvalIn(c);
			break;
		case FadeOvalOut:
			transition = newFadeOvalOut(c);
			break;
		case FadeDotIn:
			transition = newFadeDotIn(c);
			break;
		case FadeDotOut:
			transition = newFadeDotOut(c);
			break;
		case FadeTileIn:
			transition = newFadeTileIn(c);
			break;
		case FadeTileOut:
			transition = newFadeTileOut(c);
			break;
		case FadeSpiralIn:
			transition = newFadeSpiralIn(c);
			break;
		case FadeSpiralOut:
			transition = newFadeSpiralOut(c);
			break;
		case PixelDarkIn:
			transition = newPixelDarkIn(c);
			break;
		case PixelDarkOut:
			transition = newPixelDarkOut(c);
			break;
		case CrossRandom:
			transition = newCrossRandom(c);
			break;
		case SplitRandom:
			transition = newSplitRandom(c);
			break;
		case PixelWind:
			transition = newPixelWind(c);
			break;
		case PixelThunder:
			transition = newPixelThunder(c);
			break;
		}
		return transition;
	}

	/**
	 * 特效混合播放，把指定好的特效一起播放出去
	 * 
	 * @param transitions
	 * @return
	 */
	public static final LTransition newCombinedTransition(
			final TArray<LTransition> transitions) {

		if (LSystem._base != null) {

			final LTransition transition = new LTransition();

			transition.setTransitionListener(new TransitionListener() {

				@Override
				public void draw(GLEx g) {
					for (int i = 0; i < transitions.size; i++) {
						((LTransition) transitions.get(i)).draw(g);
					}
				}

				@Override
				public void update(long elapsedTime) {
					for (int i = 0; i < transitions.size; i++) {
						LTransition t = (LTransition) transitions.get(i);
						if (!t.completed()) {
							t.update(elapsedTime);
						}
					}
				}

				@Override
				public boolean completed() {
					for (int i = 0; i < transitions.size; i++) {
						if (!((LTransition) transitions.get(i)).completed()) {
							return false;
						}
					}
					return true;
				}

				@Override
				public void close() {
					for (int i = 0; i < transitions.size; i++) {
						((LTransition) transitions.get(i)).close();
					}
				}

				@Override
				public ISprite getSprite() {
					return null;
				}

			});
			transition.setDisplayGameUI(true);
			transition.code = 1;
			return transition;
		}
		return null;
	}

	/**
	 * 随机的百叶窗特效
	 * 
	 * @return
	 */
	public static final LTransition newCrossRandom() {
		return newCrossRandom(LColor.black);
	}

	/**
	 * 百叶窗特效
	 * 
	 * @param c
	 * @return
	 */
	public static final LTransition newCrossRandom(LColor c) {
		return newCross(MathUtils.random(0, 1), TextureUtils.createTexture(
				LSystem.viewSize.getWidth(), LSystem.viewSize.getHeight(), c));
	}

	/**
	 * 百叶窗特效
	 * 
	 * @param c
	 * @return
	 */
	public static final LTransition newCross(final int c, final LTexture texture) {

		if (LSystem._base != null) {

			final LTransition transition = new LTransition();

			transition.setTransitionListener(new TransitionListener() {

				final CrossEffect cross = new CrossEffect(c, texture);

				public void draw(GLEx g) {
					cross.createUI(g);
				}

				public void update(long elapsedTime) {
					cross.update(elapsedTime);
				}

				public boolean completed() {
					return cross.isCompleted();
				}

				public void close() {
					cross.close();
				}

				@Override
				public ISprite getSprite() {
					return cross;
				}

			});
			transition.setDisplayGameUI(true);
			transition.code = 1;
			return transition;
		}
		return null;
	}

	/**
	 * 默认使用黑色的圆弧渐变特效
	 * 
	 * @return
	 */
	public static final LTransition newArc() {
		return newArc(LColor.black);
	}

	/**
	 * 单一色彩的圆弧渐变特效
	 * 
	 * @return
	 */
	public static final LTransition newArc(final LColor c) {

		if (LSystem._base != null) {

			final LTransition transition = new LTransition();

			transition.setTransitionListener(new TransitionListener() {

				final ArcEffect arc = new ArcEffect(c);

				public void draw(GLEx g) {
					arc.createUI(g);
				}

				public void update(long elapsedTime) {
					arc.update(elapsedTime);
				}

				public boolean completed() {
					return arc.isCompleted();
				}

				public void close() {
					arc.close();
				}

				@Override
				public ISprite getSprite() {
					return arc;
				}

			});
			transition.setDisplayGameUI(true);
			transition.code = 1;
			return transition;
		}
		return null;
	}

	/**
	 * 产生一个Screen画面向双向分裂的过渡特效
	 * 
	 * @param texture
	 * @return
	 */
	public static final LTransition newSplitRandom(LTexture texture) {
		return newSplit(MathUtils.random(0, Config.TDOWN), texture);
	}

	/**
	 * 产生一个Screen画面向双向分裂的过渡特效
	 * 
	 * @param c
	 * @return
	 */
	public static final LTransition newSplitRandom(LColor c) {
		return newSplitRandom(TextureUtils.createTexture(
				LSystem.viewSize.getWidth(), LSystem.viewSize.getHeight(), c));
	}

	/**
	 * 产生一个Screen画面向双向分裂的过渡特效(方向的静态值位于Config类中)
	 * 
	 * @param d
	 * @param texture
	 * @return
	 */
	public static final LTransition newSplit(final int d, final LTexture texture) {

		if (LSystem._base != null) {

			final LTransition transition = new LTransition();

			transition.setTransitionListener(new TransitionListener() {

				final SplitEffect split = new SplitEffect(texture, d);

				@Override
				public void draw(GLEx g) {
					split.createUI(g);
				}

				@Override
				public void update(long elapsedTime) {
					split.update(elapsedTime);
				}

				@Override
				public boolean completed() {
					return split.isCompleted();
				}

				@Override
				public void close() {
					split.close();
				}

				@Override
				public ISprite getSprite() {
					return split;
				}

			});
			transition.setDisplayGameUI(true);
			transition.code = 1;
			return transition;
		}
		return null;
	}

	/**
	 * 产生一个黑色的淡入效果
	 * 
	 * @return
	 */
	public static final LTransition newFadeIn() {
		return newFade(FadeEffect.TYPE_FADE_IN);
	}

	/**
	 * 产生一个淡入效果
	 * 
	 * @param c
	 * @return
	 * 
	 */
	public static final LTransition newFadeIn(LColor c) {
		return newFade(FadeEffect.TYPE_FADE_IN, c);
	}

	/**
	 * 产生一个黑色的淡出效果
	 * 
	 * @return
	 */
	public static final LTransition newFadeOut() {
		return newFade(FadeEffect.TYPE_FADE_OUT);
	}

	/**
	 * 产生一个淡入效果
	 * 
	 * @param c
	 * @return
	 * 
	 */
	public static final LTransition newFadeOut(LColor c) {
		return newFade(FadeEffect.TYPE_FADE_OUT, c);
	}

	/**
	 * 产生一个黑色的淡入/淡出效果
	 * 
	 * @param type
	 * @return
	 */
	public static final LTransition newFade(int type) {
		return newFade(type, LColor.black);
	}

	/**
	 * 产生一个指定色彩的淡入效果
	 * 
	 * @param c
	 * @return
	 */
	public static final LTransition newFade(final int type, final LColor c) {
		if (LSystem._base != null) {
			final LTransition transition = new LTransition();

			transition.setTransitionListener(new TransitionListener() {

				final FadeEffect fade = FadeEffect.getInstance(type, c);

				public void draw(GLEx g) {
					fade.createUI(g);
				}

				public void update(long elapsedTime) {
					fade.update(elapsedTime);
				}

				public boolean completed() {
					return fade.isCompleted();
				}

				public void close() {
					fade.close();
				}

				@Override
				public ISprite getSprite() {
					return fade;
				}

			});
			transition.setDisplayGameUI(true);
			transition.code = 1;
			return transition;
		}
		return null;
	}

	public static final LTransition newFadeDotOut(final LColor c) {
		return newFadeDot(FadeEffect.TYPE_FADE_OUT, c);
	}

	public static final LTransition newFadeDotIn(final LColor c) {
		return newFadeDot(FadeEffect.TYPE_FADE_IN, c);
	}

	public static final LTransition newFadeDot(final int type, final LColor c) {
		if (LSystem._base != null) {
			final LTransition transition = new LTransition();

			transition.setTransitionListener(new TransitionListener() {

				final FadeDotEffect fadedot = new FadeDotEffect(type, -1, c);

				@Override
				public void draw(GLEx g) {
					fadedot.createUI(g);
				}

				@Override
				public void update(long elapsedTime) {
					fadedot.update(elapsedTime);
				}

				@Override
				public boolean completed() {
					return fadedot.isCompleted();
				}

				@Override
				public void close() {
					fadedot.close();
				}

				@Override
				public ISprite getSprite() {
					return fadedot;
				}

			});
			transition.setDisplayGameUI(true);
			transition.code = 1;
			return transition;
		}
		return null;
	}

	public static final LTransition newFadeTileOut(final LColor c) {
		return newFadeTile(FadeEffect.TYPE_FADE_OUT, c);
	}

	public static final LTransition newFadeTileIn(final LColor c) {
		return newFadeTile(FadeEffect.TYPE_FADE_IN, c);
	}

	public static final LTransition newFadeTile(final int type, final LColor c) {
		if (LSystem._base != null) {
			final LTransition transition = new LTransition();

			transition.setTransitionListener(new TransitionListener() {

				final FadeTileEffect fadetile = new FadeTileEffect(type, c);

				@Override
				public void draw(GLEx g) {
					fadetile.createUI(g);
				}

				@Override
				public void update(long elapsedTime) {
					fadetile.update(elapsedTime);
				}

				@Override
				public boolean completed() {
					return fadetile.isCompleted();
				}

				@Override
				public void close() {
					fadetile.close();
				}

				@Override
				public ISprite getSprite() {
					return fadetile;
				}

			});
			transition.setDisplayGameUI(true);
			transition.code = 1;
			return transition;
		}
		return null;
	}

	public static final LTransition newFadeSpiralOut(final LColor c) {
		return newFadeSpiral(FadeEffect.TYPE_FADE_OUT, c);
	}

	public static final LTransition newFadeSpiralIn(final LColor c) {
		return newFadeSpiral(FadeEffect.TYPE_FADE_IN, c);
	}

	public static final LTransition newFadeSpiral(final int type, final LColor c) {
		if (LSystem._base != null) {
			final LTransition transition = new LTransition();

			transition.setTransitionListener(new TransitionListener() {

				final FadeSpiralEffect fadespiral = new FadeSpiralEffect(type,
						c);

				@Override
				public void draw(GLEx g) {
					fadespiral.createUI(g);
				}

				@Override
				public void update(long elapsedTime) {
					fadespiral.update(elapsedTime);
				}

				@Override
				public boolean completed() {
					return fadespiral.isCompleted();
				}

				@Override
				public void close() {
					fadespiral.close();
				}

				@Override
				public ISprite getSprite() {
					return fadespiral;
				}

			});
			transition.setDisplayGameUI(true);
			transition.code = 1;
			return transition;
		}
		return null;
	}

	/**
	 * 产生一个黑色的淡入效果
	 * 
	 * @return
	 */
	public static final LTransition newFadeOvalIn(LColor c) {
		return newOvalFade(FadeEffect.TYPE_FADE_IN, c);
	}

	/**
	 * 产生一个黑色的淡出效果
	 * 
	 * @return
	 */
	public static final LTransition newFadeOvalOut(LColor c) {
		return newOvalFade(FadeEffect.TYPE_FADE_OUT, c);
	}

	public static final LTransition newOvalFade(final int type, final LColor c) {
		if (LSystem._base != null) {
			final LTransition transition = new LTransition();

			transition.setTransitionListener(new TransitionListener() {

				final FadeOvalEffect ovalEffect = new FadeOvalEffect(type, c);

				@Override
				public void draw(GLEx g) {
					ovalEffect.createUI(g);
				}

				@Override
				public void update(long elapsedTime) {
					ovalEffect.update(elapsedTime);
				}

				@Override
				public boolean completed() {
					return ovalEffect.isCompleted();
				}

				@Override
				public void close() {
					ovalEffect.close();
				}

				@Override
				public ISprite getSprite() {
					return ovalEffect;
				}

			});
			transition.setDisplayGameUI(true);
			transition.code = 1;
			return transition;
		}
		return null;
	}

	public static final LTransition newPixelWind(final LColor c) {
		if (LSystem._base != null) {
			final LTransition transition = new LTransition();

			transition.setTransitionListener(new TransitionListener() {

				final PixelWindEffect windEffect = new PixelWindEffect(c);

				@Override
				public void draw(GLEx g) {
					windEffect.createUI(g);
				}

				@Override
				public void update(long elapsedTime) {
					windEffect.update(elapsedTime);
				}

				@Override
				public boolean completed() {
					return windEffect.isCompleted();
				}

				@Override
				public void close() {
					windEffect.close();
				}

				@Override
				public ISprite getSprite() {
					return windEffect;
				}

			});
			transition.setDisplayGameUI(true);
			transition.code = 1;
			return transition;
		}
		return null;
	}

	public static final LTransition newPixelDarkIn(final LColor c) {
		if (LSystem._base != null) {
			final LTransition transition = new LTransition();

			transition.setTransitionListener(new TransitionListener() {

				final PixelDarkInEffect darkinEffect = new PixelDarkInEffect(c);

				@Override
				public void draw(GLEx g) {
					darkinEffect.createUI(g);
				}

				@Override
				public void update(long elapsedTime) {
					darkinEffect.update(elapsedTime);
				}

				@Override
				public boolean completed() {
					return darkinEffect.isCompleted();
				}

				@Override
				public void close() {
					darkinEffect.close();
				}

				@Override
				public ISprite getSprite() {
					return darkinEffect;
				}

			});
			transition.setDisplayGameUI(true);
			transition.code = 1;
			return transition;
		}
		return null;
	}

	public static final LTransition newPixelDarkOut(final LColor c) {
		if (LSystem._base != null) {
			final LTransition transition = new LTransition();

			transition.setTransitionListener(new TransitionListener() {

				final PixelDarkOutEffect darkoutEffect = new PixelDarkOutEffect(
						c);

				@Override
				public void draw(GLEx g) {
					darkoutEffect.createUI(g);
				}

				@Override
				public void update(long elapsedTime) {
					darkoutEffect.update(elapsedTime);
				}

				@Override
				public boolean completed() {
					return darkoutEffect.isCompleted();
				}

				@Override
				public void close() {
					darkoutEffect.close();
				}

				@Override
				public ISprite getSprite() {
					return darkoutEffect;
				}

			});
			transition.setDisplayGameUI(true);
			transition.code = 1;
			return transition;
		}
		return null;
	}

	public static final LTransition newPixelThunder(final LColor c) {
		if (LSystem._base != null) {
			final LTransition transition = new LTransition();

			transition.setTransitionListener(new TransitionListener() {

				final PixelThunderEffect thunderEffect = new PixelThunderEffect(
						c);

				@Override
				public void draw(GLEx g) {
					thunderEffect.createUI(g);
				}

				@Override
				public void update(long elapsedTime) {
					thunderEffect.update(elapsedTime);
				}

				@Override
				public boolean completed() {
					return thunderEffect.isCompleted();
				}

				@Override
				public void close() {
					thunderEffect.close();
				}

				@Override
				public ISprite getSprite() {
					return thunderEffect;
				}

			});
			transition.setDisplayGameUI(true);
			transition.code = 1;
			return transition;
		}
		return null;
	}

	public static final LTransition newEmpty() {

		final LTransition transition = new LTransition();

		transition.setTransitionListener(new TransitionListener() {

			@Override
			public void draw(GLEx g) {
			}

			@Override
			public void update(long elapsedTime) {
			}

			@Override
			public boolean completed() {
				return true;
			}

			@Override
			public void close() {
			}

			@Override
			public ISprite getSprite() {
				return null;
			}

		});

		transition.setDisplayGameUI(true);
		transition.code = 1;
		return transition;

	}

	public static interface TransitionListener extends LRelease {

		public ISprite getSprite();

		public void update(long elapsedTime);

		public void draw(GLEx g);

		public boolean completed();

		public void close();
	}

	// 是否在在启动过渡效果同时显示游戏画面（即是否顶层绘制过渡画面，底层同时绘制标准游戏画面）
	boolean isDisplayGameUI;

	int code;

	TransitionListener listener;

	public void setDisplayGameUI(boolean s) {
		this.isDisplayGameUI = s;
	}

	public boolean isDisplayGameUI() {
		return this.isDisplayGameUI;
	}

	public void setTransitionListener(TransitionListener l) {
		this.listener = l;
	}

	public TransitionListener getTransitionListener() {
		return this.listener;
	}

	final void update(long elapsedTime) {
		if (listener != null) {
			listener.update(elapsedTime);
		}
	}

	final void draw(GLEx g) {
		if (listener != null) {
			listener.draw(g);
		}
	}

	final boolean completed() {
		if (listener != null) {
			return listener.completed();
		}
		return false;
	}

	final void close() {
		if (listener != null) {
			listener.close();
		}
	}
}
