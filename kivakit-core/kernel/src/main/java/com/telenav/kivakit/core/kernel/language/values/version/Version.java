////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.values.version;

import com.telenav.kivakit.core.kernel.project.Release;

import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * Represents a <a href=https://semver.org><i>semantic version</i></a>, such as "6.3" or "1.2.1" or "6.3-rc". Supports
 * {@link #major()}, {@link #minor()}, {@link #patch()} and {@link #release()} values.
 *
 * <p><b>Parsing</b></p>
 *
 * <p>
 * Versions can be created by parsing a {@link String} with {@link #parse(String)} as well as by using the of() factory
 * methods, passing in major, minor, patch and release values.
 * </p>
 *
 * <p><b>Information</b></p>
 *
 * <ul>
 *     <li>{@link #major()} - The major version</li>
 *     <li>{@link #minor()} - The minor version</li>
 *     <li>{@link #patch()} - The optional 'dot' revision, or NO_REVISION if there is none</li>
 *     <li>{@link #release()} - The release name, or null if there is none</li>
 *     <li>{@link #hasPatch()} - True if this version has a revision value</li>
 *     <li>{@link #hasRelease()} - True if this version has a release name</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #withoutPatch()} - This version without the revision value</li>
 *     <li>{@link #withoutRelease()} - This version without the release name</li>
 * </ul>
 *
 * <p><b>Comparison</b></p>
 *
 * <ul>
 *     <li>#equals(Object) - True if the versions are equal</li>
 *     <li>{@link #isNewerThan(Version)} - True if this version is newer than the given version</li>
 *     <li>{@link #isNewerThanOrEqualTo(Version)} - True if this version is newer than or the same as the given version</li>
 *     <li>{@link #isOlderThan(Version)} - True if this version is older than the given version</li>
 *     <li>{@link #isOlderThanOrEqualTo(Version)} - True if this version is older than or the same as the given version</li>
 *     <li>{@link #newer(Version)} - The newer of this version and the given version</li>
 *     <li>{@link #older(Version)} - The older of this version and the given version</li>
 * </ul>
 *
 * <p>
 * {@link Version} objects implement the {@link #hashCode()} / {@link #equals(Object)} contract.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see <a href=https://semver.org>*Semantic Versioning*</a>
 */
public class Version
{
    /** Value for no revision */
    public static int NO_PATCH = -1;

    /** Pattern to match versions of the form [major].[minor](.[revision)?(-release)? */
    private static final Pattern PATTERN;

    static
    {
        PATTERN = Pattern.compile("(?x) "
                + "(?<major> \\d+)"
                + "\\."
                + "(?<minor> \\d+)"
                + "(\\. (?<patch> \\d+))?"
                + "(- (?<release> \\w+))?", CASE_INSENSITIVE);
    }

    /**
     * @return A version for the given major and minor values, as in 8.0
     */
    public static Version of(final int major, final int minor)
    {
        return of(major, minor, NO_PATCH);
    }

    /**
     * @return A version for the given major, minor and patch values, as in 8.0.1
     */
    public static Version of(final int major, final int minor, final int patch)
    {
        return of(major, minor, patch, null);
    }

    /**
     * @return A version for the given major, minor, patch and release values, as in 8.0.1-Beta
     */
    public static Version of(final int major, final int minor, final int patch, final Release release)
    {
        return new Version(major, minor, patch, release);
    }

    /**
     * @return The given text, of the form [major].[minor](.[revision)?(-release)?, parsed as a {@link Version} object,
     * or null if the text is not of that form.
     */
    public static Version parse(final String text)
    {
        // If the text matches the version pattern,
        final var matcher = PATTERN.matcher(text);
        if (matcher.matches())
        {
            // Extract the required major and minor versions
            final var major = Integer.parseInt(matcher.group("major"));
            final var minor = Integer.parseInt(matcher.group("minor"));

            // then get the patch group and convert it to a number or NO_PATCH if there is none
            final var patch = matcher.group("patch");
            final var patchNumber = patch == null ? NO_PATCH : Integer.parseInt(patch);

            // and the release name or null if there is none
            final var releaseName = matcher.group("release");
            final var release = releaseName == null ? null : Release.parse(releaseName);

            // and finally, construct the version object
            return of(major, minor, patchNumber, release);
        }

        return null;
    }

    private int major;

    private int minor;

    private int patch;

    private Release release;

    protected Version(final int major, final int minor, final int patch, final Release release)
    {
        this.minor = (byte) minor;
        this.major = (byte) major;
        this.patch = (byte) patch;
        this.release = release;
    }

    protected Version()
    {
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Version)
        {
            final var that = (Version) object;
            return major == that.major
                    && minor == that.minor
                    && patch == that.patch
                    && release == that.release;
        }
        return false;
    }

    public boolean hasPatch()
    {
        return patch != NO_PATCH;
    }

    public boolean hasRelease()
    {
        return release != null;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(major, minor, patch, release);
    }

    public boolean isNewerThan(final Version that)
    {
        if (major == that.major)
        {
            if (minor == that.minor)
            {
                if (patch == that.patch)
                {
                    return false;
                }
                else
                {
                    return patch > that.patch;
                }
            }
            else
            {
                return minor > that.minor;
            }
        }
        else
        {
            return major > that.major;
        }
    }

    public boolean isNewerThanOrEqualTo(final Version that)
    {
        return equals(that) || isNewerThan(that);
    }

    public boolean isOlderThan(final Version that)
    {
        return !equals(that) && !isNewerThan(that);
    }

    public boolean isOlderThanOrEqualTo(final Version that)
    {
        return equals(that) || isOlderThan(that);
    }

    public int major()
    {
        return major;
    }

    public int minor()
    {
        return minor;
    }

    public Version newer(final Version that)
    {
        return isNewerThan(that) ? this : that;
    }

    public Version older(final Version that)
    {
        return isOlderThan(that) ? this : that;
    }

    /**
     * @return The patch number, as in [major].[minor].[patch], or NO_PATCH if there is no patch number
     */
    public int patch()
    {
        return patch;
    }

    public Release release()
    {
        return release;
    }

    @Override
    public String toString()
    {
        return major + "." + minor
                + (patch == NO_PATCH ? "" : "." + patch)
                + (release == null ? "" : "-" + release.name());
    }

    /**
     * @return This version without the patch number
     */
    public Version withoutPatch()
    {
        return of(major, minor, -1, release);
    }

    /**
     * @return This version without the {@link Release}
     */
    public Version withoutRelease()
    {
        return of(major, minor, patch, null);
    }
}
